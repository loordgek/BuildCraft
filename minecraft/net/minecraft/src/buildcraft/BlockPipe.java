package net.minecraft.src.buildcraft;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_BuildCraft;

public class BlockPipe extends BlockContainer {
	
	public int texture;
	
	public BlockPipe(int i) {
		super(i, Material.ground);

		texture = ModLoader.addOverride("/terrain.png",
		"/buildcraft_gui/pipe.png");
		setResistance(3F);
	}
	
    public int getRenderType()
    {
        return mod_BuildCraft.getInstance().pipeModel;
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getBlockTextureFromSide(int i) {
    	return texture;
    }
    
    @Override
    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
		TilePipe tile = new TilePipe (i, j, k);
		world.setBlockTileEntity(i, j, k, tile);
    }

	@Override
	protected TileEntity getBlockEntity() {
		return new TilePipe ();
	}
	
	/**
	 * TODO: Factorize out this subprogram
	 */
	private boolean isPipeConnected(int id) {
		return id == mod_BuildCraft.getInstance().pipeBlock.blockID
				|| id == mod_BuildCraft.getInstance().machineBlock.blockID
				|| id == mod_BuildCraft.getInstance().filterBlock.blockID
				|| id == Block.crate.blockID
				|| id == mod_BuildCraft.getInstance().miningWellBlock.blockID;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
 {
		float xMin = Utils.pipeMinSize, xMax = Utils.pipeMaxSize, 
		yMin = Utils.pipeMinSize, yMax = Utils.pipeMaxSize, 
		zMin = Utils.pipeMinSize, zMax = Utils.pipeMaxSize;

	if (isPipeConnected (world.getBlockId(i - 1, j, k))) {
			xMin = 0.0F;
		}

		if (isPipeConnected (world.getBlockId(i + 1, j, k))) {
			xMax = 1.0F;
		}

		if (isPipeConnected (world.getBlockId(i, j - 1, k))) {
			yMin = 0.0F;
		}

		if (isPipeConnected (world.getBlockId(i, j + 1, k))) {
			yMax = 1.0F;
		}

		if (isPipeConnected (world.getBlockId(i, j, k - 1))) {
			zMin = 0.0F;
		}

		if (isPipeConnected (world.getBlockId(i, j, k + 1))) {
			zMax = 1.0F;
		}
    	
    	    
		return AxisAlignedBB.getBoundingBoxFromPool((double) i + xMin,
				(double) j + yMin, (double) k + zMin, (double) i + xMax,
				(double) j + yMax, (double) k + zMax);
    }
	
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return getCollisionBoundingBoxFromPool (world, i, j, k);
    }
}
