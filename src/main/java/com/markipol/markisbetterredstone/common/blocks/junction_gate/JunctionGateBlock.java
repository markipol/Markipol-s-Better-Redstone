package com.markipol.markisbetterredstone.common.blocks.junction_gate;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class JunctionGateBlock extends Block {
	public static final BooleanProperty NORTH_VISIBLE = BooleanProperty.create("north_visible");
	public static final BooleanProperty EAST_VISIBLE = BooleanProperty.create("east_visible");
	public static final BooleanProperty SOUTH_VISIBLE = BooleanProperty.create("south_visible");
	public static final BooleanProperty WEST_VISIBLE = BooleanProperty.create("west_visible");
	public static List<DistanceToEdge> dirs = new ArrayList<DistanceToEdge>();
	private Mode modes[] = new Mode[] { Mode.NONE, Mode.NONE, Mode.NONE, Mode.NONE };

	DecimalFormat df = new DecimalFormat("###.##");
	public static final VoxelShape HITBOX = VoxelShapes.join(Block.box(0, 0, 0, 16, 1, 16),
	        Block.box(4.5, 1, 4.5, 11.5, 2, 11.5), IBooleanFunction.OR);

	public JunctionGateBlock(Properties prop) {
		super(prop);

	}

	public JunctionGateBlock() {
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
					BlockState newState = state;

					if (!jgte.getVisibleFromDir(side)) {
						if (jgte.getNumberOfInputs() == 0) {
							// Go from not visible to input if there are 0 current inputs
							newState = jgte.setVisibleAndIO(side, pos, true, state, true);
							recalculatePower(jgte, world, pos);
							updateOutputSides(newState, world, pos, jgte);
						} else {
							// Go from not visible to output if there are 1 or more current inputs
							newState = jgte.setVisibleAndIO(side, pos, false, state, true);
							updateOutputSides(newState, world, pos, jgte);
						}
					} else {
						if (jgte.getIOFromDir(side)) {
							// Go from visible and input to visible and output
							newState = jgte.setVisibleAndIO(side, pos, false, state, true);
							recalculatePower(jgte, world, pos);
							updateOutputSides(newState, world, pos, jgte);
						} else {
							// Go from visible and output to invisible
							newState = jgte.setVisibleAndIO(side, pos, false, state, false);
							updateOutputSides(newState, world, pos, jgte);

						}
					}
					world.setBlock(pos, newState, Constants.BlockFlags.DEFAULT_AND_RERENDER);

				}
				dirs.clear();

			}

		}
		if (yClickedPos == 0.125) {
			JunctionGateTileEntity jgte = new JunctionGateTileEntity();
			if (world.getBlockEntity(pos) instanceof JunctionGateTileEntity) {
				jgte = (JunctionGateTileEntity) world.getBlockEntity(pos);
				if (jgte.getLevel().isClientSide) {
					boolean northIO = jgte.getIOFromDir(Direction.NORTH);
					boolean eastIO = jgte.getIOFromDir(Direction.EAST);
					boolean southIO = jgte.getIOFromDir(Direction.SOUTH);
					boolean westIO = jgte.getIOFromDir(Direction.WEST);

					boolean northV = jgte.getVisibleFromDir(Direction.NORTH);
					boolean eastV = jgte.getVisibleFromDir(Direction.EAST);
					boolean southV = jgte.getVisibleFromDir(Direction.SOUTH);
					boolean westV = jgte.getVisibleFromDir(Direction.WEST);

					Misc.log("C: North: V: " + northV + " IO: " + northIO + ", East V: " + eastV + "IO: " + eastIO
					        + ", South V: " + southV + "IO: " + southIO + ", West V: " + westV + "IO: " + westIO);
				}
				if (!jgte.getLevel().isClientSide) {

					boolean northIO = jgte.getIOFromDir(Direction.NORTH);
					boolean eastIO = jgte.getIOFromDir(Direction.EAST);
					boolean southIO = jgte.getIOFromDir(Direction.SOUTH);
					boolean westIO = jgte.getIOFromDir(Direction.WEST);

					boolean northV = jgte.getVisibleFromDir(Direction.NORTH);
					boolean eastV = jgte.getVisibleFromDir(Direction.EAST);
					boolean southV = jgte.getVisibleFromDir(Direction.SOUTH);
					boolean westV = jgte.getVisibleFromDir(Direction.WEST);
					Misc.log("S: North: V: " + northV + " IO: " + northIO + ", East V: " + eastV + "IO: " + eastIO
					        + ", South V: " + southV + "IO: " + southIO + ", West V: " + westV + "IO: " + westIO);
				}
			}

		}

		return ActionResultType.SUCCESS;
	}

	public void updateNeighbours(BlockState state, World world, BlockPos pos) {
		Misc.log("updateNeighbours");

		world.updateNeighborsAt(pos, this);

	}

	public void recalculatePower(JunctionGateTileEntity jgte, World world, BlockPos pos) {
		List<Direction> inputDirs = jgte.getInputDirs();
		if (inputDirs.size() == 1) {
			int power = world.getSignal(pos.relative(inputDirs.get(0)), inputDirs.get(0));
//			Misc.log(inputDirs.get(0).toString() + ", Power: "
//			        + world.getSignal(pos.relative(inputDirs.get(0)), inputDirs.get(0)) + " at pos: "
//			        + pos.relative(inputDirs.get(0)).getX() + ", " + pos.relative(inputDirs.get(0)).getY() + ", "
//			        + pos.relative(inputDirs.get(0)).getZ());
			jgte.updatePower(power, world.getBlockState(pos), pos);
		} else
			return;
	}

	public void updateOutputSides(BlockState state, World world, BlockPos gatePos, JunctionGateTileEntity jgte) {
		for (Direction d : jgte.getOutputDirs()) {
			updateSignalOnOneSide(state, world, gatePos, d);
		}
	}

	public void updateSignalOnOneSide(BlockState state, World world, BlockPos gatePos, Direction dir) {
		Misc.log("updateSignalOnOneNeighbor");
		BlockPos neighbourPos = gatePos.relative(dir.getOpposite());
		world.neighborChanged(neighbourPos, this, gatePos);
		world.updateNeighborsAt(gatePos, this);
	}

	@Override
	public int getSignal(BlockState state, IBlockReader reader, BlockPos pos, Direction directionFromNeighborToThis) {
		Direction actualDirection = directionFromNeighborToThis.getOpposite();
		JunctionGateTileEntity jgte = new JunctionGateTileEntity();
		if (reader.getBlockEntity(pos) instanceof JunctionGateTileEntity) {

			jgte = (JunctionGateTileEntity) reader.getBlockEntity(pos);
			BlockState newState = state;
			if (actualDirection == Direction.UP || actualDirection == Direction.DOWN) {
				return 0;
			} else {
				if (!jgte.getIOFromDir(actualDirection) && jgte.getVisibleFromDir(actualDirection)) {
					//Misc.log("Power requested from Tile Entity");

					return jgte.getPowerAndUpdate();

				}
			}
		}
		return 0;
	}
	
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos,
	        boolean isMoving) {
		JunctionGateTileEntity jgte = new JunctionGateTileEntity();
		if (world.getBlockEntity(pos) instanceof JunctionGateTileEntity) {
			jgte = (JunctionGateTileEntity) world.getBlockEntity(pos);
			recalculatePower(jgte, world, pos);
//			updateOutputSides(state, world, pos, jgte);
		}
		
		return;
	}

	@Override
	public boolean isSignalSource(BlockState p_149744_1_) {

		return true;
	}

	public static BooleanProperty dirToProperty(Direction d) {
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
//	
//	public void toggleMode(Direction side) {
//		switch (modes)
//	}

	public enum Mode implements IStringSerializable {
		NONE("none"), INPUT("input"), OUTPUT("output");

		private final String name;

		Mode(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {

			return name;
		}

		@Override
		public String toString() {

			return getSerializedName();
		}

	}

}
