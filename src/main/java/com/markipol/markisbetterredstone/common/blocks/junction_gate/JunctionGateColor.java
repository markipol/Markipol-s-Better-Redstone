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



			// Redstone Wire color (red)
			if (tintindex == 0) {

				int power = jgte.getPower();
				return RedstoneWireBlock.getColorForPower(power);

			}
			// North Entry Gate
			if (tintindex == 1) {
				if (jgte.getVisibleFromDir(Direction.NORTH)) {
					return jgte.getIOFromDir(Direction.NORTH) ? BLUE : ORANGE;
				}
				else return new Color(12, 240, 42).getRGB();
				//Misc.log("Purple");


			}
			// East Entry
			if (tintindex == 2) {
				if (jgte.getVisibleFromDir(Direction.EAST)) {
					return jgte.getIOFromDir(Direction.EAST) ? BLUE : ORANGE;
				}
				else return new Color(12, 240, 42).getRGB();
			}
			// South Entry
			if (tintindex == 3) {
				if (jgte.getVisibleFromDir(Direction.SOUTH)) {
					return jgte.getIOFromDir(Direction.SOUTH) ? BLUE : ORANGE;
				}
				else return new Color(12, 240, 42).getRGB();
			}
			// West Entry
			if (tintindex == 4) {
				if (jgte.getVisibleFromDir(Direction.WEST)) {
					return jgte.getIOFromDir(Direction.WEST) ? BLUE : ORANGE;
				}
				else return new Color(12, 240, 42).getRGB();

			}
		}
		return new Color(120, 66, 245).getRGB();
	}

}
