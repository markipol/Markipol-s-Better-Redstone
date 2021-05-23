package com.markipol.markisbetterredstone.common.blocks.junction_gate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.markipol.markisbetterredstone.util.Misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class JunctionGateBlock extends Block {
	public static final BooleanProperty NORTH_VISIBLE = BooleanProperty.create("north_visible");
	public static final BooleanProperty EAST_VISIBLE = BooleanProperty.create("east_visible");
	public static final BooleanProperty SOUTH_VISIBLE = BooleanProperty.create("south_visible");
	public static final BooleanProperty WEST_VISIBLE = BooleanProperty.create("west_visible");
	public static List<DistanceToEdge> dirs = new ArrayList<DistanceToEdge>();

	DecimalFormat df = new DecimalFormat("###.##");
	public static final VoxelShape HITBOX = VoxelShapes.join(Block.box(0, 0, 0, 16, 1, 16),
	        Block.box(4.5, 1, 4.5, 11.5, 2, 11.5), IBooleanFunction.OR);

	public JunctionGateBlock(Properties prop) {
		super(prop);

	}

	public JunctionGateBlock() {
		// super(Block.Properties.of(Material.BUILDABLE_GLASS).noOcclusion().lightLevel(ColoredLampBlock::getLightValue));
		super(Block.Properties.of(Material.STONE).noOcclusion());

	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
	        ISelectionContext p_220053_4_) {

		return HITBOX;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {

		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {

		return new JunctionGateTileEntity();
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {

		builder.add(NORTH_VISIBLE, EAST_VISIBLE, SOUTH_VISIBLE, WEST_VISIBLE);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {

		return defaultBlockState().setValue(NORTH_VISIBLE, false).setValue(EAST_VISIBLE, false)
		        .setValue(SOUTH_VISIBLE, false).setValue(WEST_VISIBLE, false);
	}

	@Override
	public void onPlace(BlockState state, World world, BlockPos pos, BlockState state2, boolean p_220082_5_) {

		TileEntity te = world.getBlockEntity(pos);
		if (te instanceof JunctionGateTileEntity) {
			JunctionGateTileEntity jgte = (JunctionGateTileEntity) te;
			jgte.inputDirs.clear();
			jgte.outputDirs.clear();
		}
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
	        BlockRayTraceResult ray) {
//		int xBP = pos.getX();
//		int yBP = pos.getY();
//		int zBP = pos.getZ();
//		if (xBP > 0 && yBP > 0) {
//			double xClickedPos = (ray.getLocation().x)-xBP;
//			double yClickedPos = (ray.getLocation().y)-yBP;
//		}
		

			double xClickedPos = ray.getLocation().x - pos.getX();
			double yClickedPos = ray.getLocation().y - pos.getY();
			double zClickedPos = ray.getLocation().z - pos.getZ();

			
			if (!world.isClientSide) {
			if (yClickedPos == 0.0625) {
				double distanceToEastRedEdge = 1 - xClickedPos;
				double distanceToWestYellowEdge = xClickedPos;
				double distanceToNorthGreenEdge = zClickedPos;
				double distanceToSouthBlueEdge = 1 - zClickedPos;
				dirs.add(new DistanceToEdge(Direction.NORTH, distanceToNorthGreenEdge));
				dirs.add(new DistanceToEdge(Direction.EAST, distanceToEastRedEdge));
				dirs.add(new DistanceToEdge(Direction.SOUTH, distanceToSouthBlueEdge));
				dirs.add(new DistanceToEdge(Direction.WEST, distanceToWestYellowEdge));
				Collections.sort(dirs);
				Direction side = dirs.get(0).getDirection();
				// Misc.log(dirs.get(0).dir.toString());
				JunctionGateTileEntity jgte = new JunctionGateTileEntity();
				if (world.getBlockEntity(pos) instanceof JunctionGateTileEntity) {
					// Misc.log("So it exists, at least...");
					jgte = (JunctionGateTileEntity) world.getBlockEntity(pos);

					if (jgte.isInput(side)) {
						Misc.log("This side is an input, changing to output");
						jgte.setOutput(side, pos);
					} else if (jgte.isOutput(side)) {
						Misc.log("This side is an output, changing to empty");
						jgte.setEmpty(side, pos);
						world.setBlockAndUpdate(pos, state.setValue(dirToProperty(side), false));
						jgte.sW(pos);
					} else {
						Misc.log("This side is empty, changing to input");
						jgte.setInput(side, pos);
						world.setBlockAndUpdate(pos, state.setValue(dirToProperty(side), true));
					}

					boolean north = jgte.inputDirs.contains(Direction.NORTH)
					        || jgte.outputDirs.contains(Direction.NORTH);
					boolean east = jgte.inputDirs.contains(Direction.EAST) || jgte.outputDirs.contains(Direction.EAST);
					boolean south = jgte.inputDirs.contains(Direction.SOUTH)
					        || jgte.outputDirs.contains(Direction.SOUTH);
					boolean west = jgte.outputDirs.contains(Direction.WEST) || jgte.outputDirs.contains(Direction.WEST);

//					Misc.log("North visible: " + north + " East visible: " + east + " South visible: " + south
//					        + " West visible: " + west);

				}
				dirs.clear();

				//Misc.log("X: " + df.format(xClickedPos) + " Y: " + yClickedPos + " Z: " + df.format(zClickedPos));

			}


		}
			if (yClickedPos == 0.125) {
				JunctionGateTileEntity jgte = new JunctionGateTileEntity();
				if (world.getBlockEntity(pos) instanceof JunctionGateTileEntity) {
					jgte = (JunctionGateTileEntity) world.getBlockEntity(pos);
					if (jgte.getLevel().isClientSide) {
						boolean northI = jgte.inputDirs.contains(Direction.NORTH);
						boolean eastI = jgte.inputDirs.contains(Direction.EAST);
						boolean southI = jgte.inputDirs.contains(Direction.SOUTH);
						boolean westI = jgte.inputDirs.contains(Direction.WEST);

						boolean northO = jgte.outputDirs.contains(Direction.NORTH);
						boolean eastO = jgte.outputDirs.contains(Direction.EAST);
						boolean southO = jgte.outputDirs.contains(Direction.SOUTH);
						boolean westO = jgte.outputDirs.contains(Direction.WEST);
						
						String northS = "Empty";
						String eastS = "Empty";
						String westS = "Empty";
						String southS = "Empty";
						
						if (northI) northS = "an Input"; if (northO) northS = "an Output";
						if (eastI) eastS = "an Input"; if (eastO) eastS = "an Output";
						if (southI) southS = "an Input"; if (southO) southS = "an Output";
						if (westI) westS = "an Input"; if (westO) westS = "an Output";
						
Misc.log("On the client, North is " + northS + ", East is " + eastS + ", South is " + southS + ", West is " + westS);
					}
					if (!jgte.getLevel().isClientSide) {
						boolean northI = jgte.inputDirs.contains(Direction.NORTH);
						boolean eastI = jgte.inputDirs.contains(Direction.EAST);
						boolean southI = jgte.inputDirs.contains(Direction.SOUTH);
						boolean westI = jgte.inputDirs.contains(Direction.WEST);

						boolean northO = jgte.outputDirs.contains(Direction.NORTH);
						boolean eastO = jgte.outputDirs.contains(Direction.EAST);
						boolean southO = jgte.outputDirs.contains(Direction.SOUTH);
						boolean westO = jgte.outputDirs.contains(Direction.WEST);
						
						String northS = "Empty";
						String eastS = "Empty";
						String westS = "Empty";
						String southS = "Empty";
						
						if (northI) northS = "an Input"; if (northO) northS = "an Output";
						if (eastI) eastS = "an Input"; if (eastO) eastS = "an Output";
						if (southI) southS = "an Input"; if (southO) southS = "an Output";
						if (westI) westS = "an Input"; if (westO) westS = "an Output";
						
Misc.log("On the server, North is " + northS + ", East is " + eastS + ", South is " + southS + ", West is " + westS);
					}
				}
			}
		return ActionResultType.SUCCESS;
	}

	public BooleanProperty dirToProperty(Direction d) {
		if (d == Direction.NORTH)
			return NORTH_VISIBLE;
		if (d == Direction.EAST)
			return EAST_VISIBLE;
		if (d == Direction.SOUTH)
			return SOUTH_VISIBLE;
		if (d == Direction.WEST)
			return WEST_VISIBLE;
		return NORTH_VISIBLE;
	}

}
