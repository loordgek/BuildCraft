package buildcraft.lib.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import buildcraft.api.core.BCDebugging;
import buildcraft.api.core.BCLog;
import buildcraft.lib.CreativeTabManager;
import buildcraft.lib.MigrationManager;
import buildcraft.lib.RegistryHelper;
import buildcraft.lib.TagManager;
import buildcraft.lib.TagManager.EnumTagType;
import buildcraft.lib.TagManager.EnumTagTypeMulti;

import gnu.trove.map.hash.TIntObjectHashMap;

public class ItemBuildCraft_BC8 extends Item {
    private static final boolean DEBUG = BCDebugging.shouldDebugLog("lib.item");

    private static List<ItemBuildCraft_BC8> registeredItems = new ArrayList<>();

    /** The tag used to identify this in the {@link TagManager} */
    public final String id;

    public ItemBuildCraft_BC8(String id) {
        this.id = id;
        setUnlocalizedName(TagManager.getTag(id, EnumTagType.UNLOCALIZED_NAME));
        setRegistryName(TagManager.getTag(id, EnumTagType.REGISTRY_NAME));
        setCreativeTab(CreativeTabManager.getTab(TagManager.getTag(id, EnumTagType.CREATIVE_TAB)));
    }

    public static <I extends ItemBuildCraft_BC8> I register(I item) {
        return register(item, false);
    }

    public static <I extends ItemBuildCraft_BC8> I register(I item, boolean force) {
        if (RegistryHelper.registerItem(item, force)) {
            registeredItems.add(item);
            MigrationManager.INSTANCE.addItemMigration(item, TagManager.getMultiTag(item.id, EnumTagTypeMulti.OLD_REGISTRY_NAME));
            return item;
        }
        return null;
    }

    public static void fmlInit() {
        for (ItemBuildCraft_BC8 item : registeredItems) {
            if (TagManager.hasTag(item.id, EnumTagType.OREDICT_NAME)) {
                OreDictionary.registerOre(TagManager.getTag(item.id, EnumTagType.OREDICT_NAME), item);
            }
        }
    }

    /** Sets up all of the model information for this item. This is called multiple times, and you *must* make sure that
     * you add all the same values each time. Use {@link #addVariant(TIntObjectHashMap, int, String)} to help get
     * everything correct. */
    @SideOnly(Side.CLIENT)
    protected void addModelVariants(TIntObjectHashMap<ModelResourceLocation> variants) {
        addVariant(variants, 0, "");
    }

    public void addVariant(TIntObjectHashMap<ModelResourceLocation> variants, int meta, String suffix) {
        String tag = TagManager.getTag(id, EnumTagType.MODEL_LOCATION);
        variants.put(meta, new ModelResourceLocation(tag + suffix, "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public final void postRegisterClient() {
        TIntObjectHashMap<ModelResourceLocation> variants = new TIntObjectHashMap<>();
        addModelVariants(variants);
        for (ModelResourceLocation variant : variants.values(new ModelResourceLocation[variants.size()])) {
            if (DEBUG) {
                BCLog.logger.info("[lib.item][" + getRegistryName() + "] Registering a variant " + variant);
            }
            ModelBakery.registerItemVariants(this, variant);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void fmlInitClient() {
        for (ItemBuildCraft_BC8 item : registeredItems) {
            TIntObjectHashMap<ModelResourceLocation> variants = new TIntObjectHashMap<>();
            item.addModelVariants(variants);
            for (int meta : variants.keys()) {
                ModelResourceLocation mrl = variants.get(meta);
                if (DEBUG) {
                    BCLog.logger.info("[lib.item][" + item.getRegistryName() + "] Registering a variant " + meta + " -> " + mrl);
                }
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, mrl);
            }
        }
    }
}