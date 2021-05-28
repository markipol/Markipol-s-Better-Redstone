package com.markipol.markisbetterredstone.common.blocks.magic_block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class MagicBlock extends Block {

	public MagicBlock(Properties p_i48440_1_) {
		super(p_i48440_1_);
		
	}
	
	public MagicBlock() {
		super(Properties.of(Material.CLAY).sound(SoundType.GLASS).strength(2.0f).noOcclusion());
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		
		return new MagicBlockTileEntity();
	}
	

//	@Override
//	public VoxelShape getOcclusionShape(BlockState p_196247_1_, IBlockReader p_196247_2_, BlockPos p_196247_3_) {
//		
//		return VoxelShapes.empty();
//	}
//	@Override
//	public BlockRenderType getRenderShape(BlockState p_149645_1_) {
//		
//		return BlockRenderType.INVISIBLE;
//	}

}
