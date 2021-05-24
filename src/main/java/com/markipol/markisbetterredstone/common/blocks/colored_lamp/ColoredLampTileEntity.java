package com.markipol.markisbetterredstone.common.blocks.colored_lamp;

import javax.annotation.Nullable;

import com.markipol.markisbetterredstone.Reg;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ColoredLampTileEntity extends TileEntity{
	public int redIntensity;
	public int greenIntensity;
	public int blueIntensity;
	public int light;

	public ColoredLampTileEntity(TileEntityType<? extends TileEntity> type) {
		super(type);
		
	}
	
	public ColoredLampTileEntity() {
		this(Reg.COLORED_LAMP_TILE_ENTITY.get());
	}
	
	public void updateColors(int red, int green, int blue) {
		redIntensity = red;
		greenIntensity = green;
		blueIntensity = blue;
	}
	public void updateLight(int light) {
		this.light = light;
	}
	public int getLight() {
		return this.light;
	}
	public int getRed() {
		return redIntensity;
	}
	public int getGreen() {
		return greenIntensity;
		
	}
	public int getBlue() {
		return blueIntensity;
	}
	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {

		CompoundNBT nbt = getUpdateTag();
		return new SUpdateTileEntityPacket(this.getBlockPos(), 6, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {

		BlockState state = level.getBlockState(getBlockPos());
		load(state, pkt.getTag());

	}

	@Override
	public CompoundNBT getUpdateTag() {

		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {

		this.load(state, tag);
	}

	@Override
	public CompoundNBT save(CompoundNBT parentNBT) {

		super.save(parentNBT);
		parentNBT.putInt("redIntensity", redIntensity);
		parentNBT.putInt("greenIntensity", greenIntensity);
		parentNBT.putInt("blueIntensity", blueIntensity);
		parentNBT.putInt("light", light);

		return parentNBT;
	}

	@Override
	public void load(BlockState state, CompoundNBT parentNBT) {
		super.load(state, parentNBT);
		redIntensity = parentNBT.getInt("redIntensity");
		greenIntensity = parentNBT.getInt("greenIntensity");
		blueIntensity = parentNBT.getInt("blueIntensity");
		light = parentNBT.getInt("light");
		


	}


}
