package com.markipol.markisbetterredstone.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class CornerGateBlock extends HorizontalBlock {
	public static final Property<Direction> INPUT = DirectionProperty.create("input", Direction.Plane.HORIZONTAL);
	public static final Property<Direction> OUTPUT = DirectionProperty.create("output1", Direction.Plane.HORIZONTAL);

	public CornerGateBlock(Properties prop) {
		super(prop);

	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {

		//builder.add(BlockStateProperties.POWER, INPUT, OUTPUT);
	}

}
