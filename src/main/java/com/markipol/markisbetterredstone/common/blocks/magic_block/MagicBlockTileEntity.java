package com.markipol.markisbetterredstone.common.blocks.magic_block;

import com.markipol.markisbetterredstone.Reg;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

public class MagicBlockTileEntity extends TileEntity {

	public MagicBlockTileEntity(TileEntityType<? extends TileEntity> type) {
		super(type);
		
	}
	public MagicBlockTileEntity() {
		this(Reg.MAGIC_BLOCK_TILE_ENTITY.get());
	}
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		
		return new AxisAlignedBB(getBlockPos(), getBlockPos().offset(1,3,1));
	}

}
