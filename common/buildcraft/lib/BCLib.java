/* Copyright (c) 2016 SpaceToad and the BuildCraft team
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package buildcraft.lib;

import java.util.function.Consumer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import buildcraft.api.BCModules;
import buildcraft.api.core.BCLog;
import buildcraft.api.core.BuildCraftAPI;
import buildcraft.api.fuels.BuildcraftFuelRegistry;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;

import buildcraft.lib.block.VanillaPaintHandlers;
import buildcraft.lib.block.VanillaRotationHandlers;
import buildcraft.lib.fluids.CoolantRegistry;
import buildcraft.lib.fluids.FuelRegistry;
import buildcraft.lib.item.ItemManager;
import buildcraft.lib.list.ListMatchHandlerFluid;
import buildcraft.lib.list.VanillaListHandlers;
import buildcraft.lib.marker.MarkerCache;
import buildcraft.lib.misc.FakePlayerUtil;
import buildcraft.lib.net.cache.BuildCraftObjectCaches;
import buildcraft.lib.recipe.AssemblyRecipeRegistry;
import buildcraft.lib.recipe.IntegrationRecipeRegistry;
import buildcraft.lib.registry.TagManager;
import buildcraft.lib.registry.TagManager.EnumTagType;
import buildcraft.lib.registry.TagManager.TagEntry;

//@formatter:off
@Mod(modid = BCLib.MODID,
     name = "BuildCraft Lib",
     version = BCLib.VERSION,
     acceptedMinecraftVersions = "[1.11.2]",
     dependencies = "required-after:forge@[13.19.0.2176,)")
//@formatter:on
public class BCLib {
    public static final String MODID = "buildcraftlib";
    public static final String VERSION = "{$version}";
    public static final String MC_VERSION = "{$mc_version}";

    @Instance(MODID)
    public static BCLib INSTANCE;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent evt) {
        BCLog.logger.info("");
        BCLog.logger.info("Starting BuildCraft " + BCLib.VERSION);
        BCLog.logger.info("Copyright (c) the BuildCraft team, 2011-2016");
        BCLog.logger.info("http://www.mod-buildcraft.com");
        BCLog.logger.info("");
        BCModules.fmlPreInit();

        BuildcraftRecipeRegistry.assemblyRecipes = AssemblyRecipeRegistry.INSTANCE;
        BuildcraftRecipeRegistry.integrationRecipes = IntegrationRecipeRegistry.INSTANCE;
        BuildcraftFuelRegistry.fuel = FuelRegistry.INSTANCE;
        BuildcraftFuelRegistry.coolant = CoolantRegistry.INSTANCE;
        BuildCraftAPI.fakePlayerProvider = FakePlayerUtil.INSTANCE;

        BCLibProxy.getProxy().fmlPreInit();

        BCLibItems.preInit();

        BuildCraftObjectCaches.fmlPreInit();
        BCMessageHandler.fmlPreInit();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, BCLibProxy.getProxy());

        MinecraftForge.EVENT_BUS.register(BCLibEventDist.INSTANCE);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent evt) {
        BCLibProxy.getProxy().fmlInit();

        VanillaListHandlers.fmlInit();
        VanillaPaintHandlers.fmlInit();
        VanillaRotationHandlers.fmlInit();

        ItemManager.fmlInit();

        BCLibRecipes.fmlInit();

        BCLibDatabase.fmlInit();
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent evt) {
        BCLibProxy.getProxy().fmlPostInit();
        BuildCraftObjectCaches.fmlPostInit();
        BCMessageHandler.fmlPostInit();
        VanillaListHandlers.fmlPostInit();
        MarkerCache.postInit();
        ListMatchHandlerFluid.fmlPostInit();
    }

    @Mod.EventHandler
    public static void onServerStarted(FMLServerStartedEvent evt) {
        BCLibEventDist.onServerStarted(evt);
    }

    static {
        startBatch();
        registerTag("item.guide").reg("guide").locale("guide").model("guide").tab("vanilla.misc");
        registerTag("item.debugger").reg("debugger").locale("debugger").model("debugger").tab("vanilla.misc");
        endBatch(TagManager.prependTags("buildcraftlib:", EnumTagType.REGISTRY_NAME, EnumTagType.MODEL_LOCATION));
    }

    private static TagEntry registerTag(String id) {
        return TagManager.registerTag(id);
    }

    private static void startBatch() {
        TagManager.startBatch();
    }

    private static void endBatch(Consumer<TagEntry> consumer) {
        TagManager.endBatch(consumer);
    }
}
