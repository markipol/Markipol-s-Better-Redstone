package com.markipol.markisbetterredstone.common.blocks.colored_lamp;

import java.awt.Color;

import javax.annotation.Nullable;

import com.markipol.markisbetterredstone.util.Misc;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ColoredLampBlock extends Block {
	private static final int MIN_INTENSITY = 0;
	private static final int MAX_INTENSITY = 4;
	public static final Property<Direction> DIRECTION_OF_UNCONNECTED_FACE = DirectionProperty
	        .create("direction_of_unconnected_face", Direction.Plane.HORIZONTAL);
	private static final IntegerProperty RED_INTENSITY = IntegerProperty.create("red_intensity", MIN_INTENSITY,
	        MAX_INTENSITY);
	private static final IntegerProperty GREEN_INTENSITY = IntegerProperty.create("green_intensity", MIN_INTENSITY,
	        MAX_INTENSITY);
	private static final IntegerProperty BLUE_INTENSITY = IntegerProperty.create("blue_intensity", MIN_INTENSITY,
	        MAX_INTENSITY);

	public ColoredLampBlock() {
		super(Block.Properties.of(Material.BUILDABLE_GLASS).lightLevel(ColoredLampBlock::getLightValue).noOcclusion());

	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {

		builder.add(DIRECTION_OF_UNCONNECTED_FACE, RED_INTENSITY, GREEN_INTENSITY, BLUE_INTENSITY);
	}

	@Override
	public boolean isSignalSource(BlockState p_149744_1_) {

		return false;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {

		BlockState state = this.defaultBlockState();
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction[] directions = context.getNearestLookingDirections();
		for (Direction direction : directions) {
			if (direction.getAxis().isHorizontal()) {
				state = state.setValue(DIRECTION_OF_UNCONNECTED_FACE, direction);
				if (state.canSurvive(world, pos)) {
					return state;
				}
			}
		}
		return this.defaultBlockState();
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {

		super.setPlacedBy(world, pos, state, placer, stack);
		BlockState nbs = getLampColourFromInputs(world, pos, state);
		world.setBlock(pos, nbs, Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
	}

	@Override
	public void neighborChanged(BlockState currentState, World world, BlockPos pos, Block p_220069_4_,
	        BlockPos p_220069_5_, boolean p_220069_6_) {

		BlockState nbs = getLampColourFromInputs(world, pos, currentState);
		if (nbs != currentState) {
			world.setBlock(pos, nbs, Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
		}
		if (world.getBlockEntity(pos) instanceof ColoredLampTileEntity) {
			ColoredLampTileEntity clte = (ColoredLampTileEntity) world.getBlockEntity(pos);
			updateTileEntity(world, pos, currentState, clte);
		}

	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
	        ISelectionContext p_220053_4_) {

		return VoxelShapes.block();
	}

	public static int getLightValue(BlockState state) {
		int red = state.getValue(RED_INTENSITY);
		int green = state.getValue(GREEN_INTENSITY);
		int blue = state.getValue(BLUE_INTENSITY);
		int brightest = Math.max(Math.max(red, green), blue);

		int lightValue = (int) Misc.scale2scale(brightest, 0, 4, 0, 15);
		Misc.log(Integer.toString(lightValue));
		return lightValue;
	}

	public static int getRGBLampColour(BlockState state) {
		int red = state.getValue(RED_INTENSITY);
		int green = state.getValue(GREEN_INTENSITY);
		int blue = state.getValue(BLUE_INTENSITY);
		int redRGB = (int) Misc.scale2scale(red, 0, 4, 0, 255);
		int greenRGB = (int) Misc.scale2scale(green, 0, 4, 0, 255);
		int blueRGB = (int) Misc.scale2scale(blue, 0, 4, 0, 255);
		Color lampColour = new Color(redRGB, greenRGB, blueRGB);
		return lampColour.getRGB();

	}

	private BlockState getLampColourFromInputs(World world, BlockPos pos, BlockState state) {
		Direction facing = state.getValue(DIRECTION_OF_UNCONNECTED_FACE);
		Direction redDir = facing.getCounterClockWise();
		Direction greenDir = facing.getOpposite();
		Direction blueDir = facing.getClockWise();

		int redPower = world.getSignal(pos.relative(redDir), redDir);
		int greenPower = world.getSignal(pos.relative(greenDir), greenDir);
		int bluePower = world.getSignal(pos.relative(blueDir), blueDir);

		float redIntensity = Misc.scale2scale(redPower, 0, 15, 0, 4);
		float blueIntensity = Misc.scale2scale(bluePower, 0, 15, 0, 4);
		float greenIntensity = Misc.scale2scale(greenPower, 0, 15, 0, 4);

		return state.setValue(RED_INTENSITY, (int) Math.round(redIntensity))
		        .setValue(GREEN_INTENSITY, (int) Math.round(greenIntensity))
		        .setValue(BLUE_INTENSITY, (int) Math.round(blueIntensity));
	}

	private void updateTileEntity(World world, BlockPos pos, BlockState state, ColoredLampTileEntity clte) {
		Direction facing = state.getValue(DIRECTION_OF_UNCONNECTED_FACE);
		Direction redDir = facing.getCounterClockWise();
		Direction greenDir = facing.getOpposite();
		Direction blueDir = facing.getClockWise();

		int redPower = world.getSignal(pos.relative(redDir), redDir);
		int greenPower = world.getSignal(pos.relative(greenDir), greenDir);
		int bluePower = world.getSignal(pos.relative(blueDir), blueDir);

		clte.updateColors(redPower, greenPower, bluePower);
		Misc.log("Red: " + Integer.toString(clte.getRed()) + " Green: " + Integer.toString(clte.getGreen()) + " Blue: "
		        + Integer.toString(clte.getBlue()));

	}

	@Override
	public ActionResultType use(BlockState p_225533_1_, World world, BlockPos pos, PlayerEntity p_225533_4_,
	        Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {

		if (!world.isClientSide) {
			Misc.log("Use");
			if (world.getBlockEntity(pos) instanceof ColoredLampTileEntity) {
				ColoredLampTileEntity clte = (ColoredLampTileEntity) world.getBlockEntity(pos);
				Misc.log("Red: " + Integer.toString(clte.getRed()) + " Green: " + Integer.toString(clte.getGreen())
				        + " Blue: " + Integer.toString(clte.getBlue()));
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {

		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {

		return new ColoredLampTileEntity();
	}

}
