//package com.markipol.markisbetterredstone.common.blocks.fancy_block;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.SoundType;
//import net.minecraft.block.material.Material;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.BlockRayTraceResult;
//import net.minecraft.util.math.shapes.ISelectionContext;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.util.math.shapes.VoxelShapes;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//
//public class FancyBlock extends Block {
//	
//
//	public FancyBlock(Properties p_i48440_1_) {
//		super(p_i48440_1_);
//
//	}
//
//	public FancyBlock() {
//		super(Properties.of(Material.CAKE).sound(SoundType.METAL).strength(2.0f).noOcclusion());
//	}
//
//	@Override
//	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
//		TileEntity te = world.getBlockEntity(pos);
//		if (te instanceof FancyBlockTE) {
//			FancyBlockTE fbte = (FancyBlockTE) te;
//			BlockState mimic = fbte.getMimic();
//			if (mimic != null) {
//				return mimic.getLightValue(world, pos);
//			}
//		}
//		return super.getLightValue(state, world, pos);
//	}
//
//	@Override
//	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
//		TileEntity te = world.getBlockEntity(pos);
//		if (te instanceof FancyBlockTE) {
//			FancyBlockTE fbte = (FancyBlockTE) te;
//			BlockState mimic = fbte.getMimic();
//			if (mimic != null ) {
//				return mimic.getShape(world, pos, context);
//			}
//		}
//		return super.getShape(state, world, pos, context);
//	}
//
//	@Override
//	public boolean hasTileEntity(BlockState state) {
//
//		return true;
//	}
//
//	@Override
//	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
//
//		return new FancyBlockTE();
//	}
//
//	@Override
//	public ActionResultType use(BlockState shape, World world, BlockPos pos, PlayerEntity player, Hand hand,
//	        BlockRayTraceResult ray) {
//		ItemStack item = player.getItemInHand(hand);
//		if (!item.isEmpty() && item.getItem() instanceof BlockItem) {
//			if (!world.isClientSide) {
//				TileEntity te = world.getBlockEntity(pos);
//				if (te instanceof FancyBlockTE) {
//					FancyBlockTE fbte = (FancyBlockTE) te;
//
//					BlockState newMimic = ((BlockItem) item.getItem()).getBlock().defaultBlockState();
//					fbte.setMimic(newMimic);
//				}
//			}
//			return ActionResultType.SUCCESS;
//		}
//
//		return super.use(shape, world, pos, player, hand, ray);
//	}
//
//}
