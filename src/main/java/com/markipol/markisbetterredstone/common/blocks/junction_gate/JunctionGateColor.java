package com.markipol.markisbetterredstone.common.blocks.junction_gate;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.markipol.markisbetterredstone.util.Misc;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class JunctionGateColor implements IBlockColor {
	public JunctionGateTileEntity jgte;
	public List<Direction> inputDirs = new ArrayList<Direction>();
	public List<Direction> outputDirs = new ArrayList<Direction>();
	public static final int BLUE = new Color(252, 186, 3).getRGB();
	public static final int ORANGE = new Color(3, 186, 252).getRGB();

	@Override
	public int getColor(BlockState state, IBlockDisplayReader iLightReader, BlockPos pos, int tintindex) {
		
		if (iLightReader.getBlockEntity(pos) instanceof JunctionGateTileEntity) {
			//Misc.log("Does it show, that is the question");
			JunctionGateTileEntity jgte = (JunctionGateTileEntity) iLightReader.getBlockEntity(pos);

			// if (!inputDirs.isEmpty())
			inputDirs = jgte.getInputDirs();
			// if (!outputDirs.isEmpty())
			outputDirs = jgte.getOutputDirs();

			// Redstone Wire color (red)
			if (tintindex == 0) {

				int power = jgte.getPower();
				return RedstoneWireBlock.getColorForPower(power);

			}
			// North Entry Gate
			if (tintindex == 1) {
				//Misc.log("Purple");
				if (inputDirs.contains(Direction.NORTH)) {

					return BLUE;
				}
				if (outputDirs.contains(Direction.NORTH)) {
					return ORANGE;
				}

			}
			// East Entry
			if (tintindex == 2) {
				if (inputDirs.contains(Direction.EAST)) {

					return BLUE;
				}
				if (outputDirs.contains(Direction.EAST)) {
					return ORANGE;
				}
			}
			// South Entry
			if (tintindex == 3) {
				if (inputDirs.contains(Direction.SOUTH)) {

					return BLUE;
				}
				if (outputDirs.contains(Direction.SOUTH)) {
					return ORANGE;
				}
			}
			// West Entry
			if (tintindex == 4) {
				if (inputDirs.contains(Direction.WEST)) {

					return BLUE;
				}
				if (outputDirs.contains(Direction.WEST)) {
					return ORANGE;
				}

			}
		}
		return new Color(120, 66, 245).getRGB();
	}

}
