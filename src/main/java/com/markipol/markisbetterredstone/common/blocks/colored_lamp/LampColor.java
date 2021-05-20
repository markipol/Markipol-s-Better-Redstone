package com.markipol.markisbetterredstone.common.blocks.colored_lamp;

import com.markipol.markisbetterredstone.util.Misc;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class LampColor implements net.minecraft.client.renderer.color.IBlockColor{

	@Override
	public int getColor(BlockState state, IBlockDisplayReader iLightReader, BlockPos blockPos,
	        int tintIndex) {
		
		//Misc.log("The wheels are turning");
		
		if (iLightReader.getBlockEntity(blockPos) instanceof ColoredLampTileEntity) {
			ColoredLampTileEntity clte = (ColoredLampTileEntity)iLightReader.getBlockEntity(blockPos);
			Misc.log("The tile entity's wheels are turning");
			return ColoredLampBlock.getRGBFromTE(clte);
		}
		return ColoredLampBlock.getRGBLampColour(state);
	}

}
