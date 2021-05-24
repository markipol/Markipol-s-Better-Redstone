package com.markipol.markisbetterredstone.common.blocks.junction_gate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.markipol.markisbetterredstone.Reg;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class JunctionGateTileEntity extends TileEntity {
	public List<Direction> inputDirs = new ArrayList<Direction>();
	public List<Direction> outputDirs = new ArrayList<Direction>();
	public boolean northVisible = false;
	public boolean eastVisible = false;
	public boolean southVisible = false;
	public boolean westVisible = false;

	public boolean northIO = false;
	public boolean eastIO = false;
	public boolean southIO = false;
	public boolean westIO = false;
	public int power = 0;

	public JunctionGateTileEntity(TileEntityType<? extends TileEntity> type) {
		super(type);

	}

	public JunctionGateTileEntity() {
		this(Reg.JUNCTION_GATE_TILE_ENTITY.get());
	}

	public List<Direction> getInputDirs() {

		return inputDirs;
	}

	public boolean isInput(Direction d) {
		return inputDirs.contains(d);
	}

	public boolean isOutput(Direction d) {
		return outputDirs.contains(d);
	}

	public boolean falseForOutputTrueForInput(Direction d) {
		return getIOFromDir(d);
	}
	

	//sW stands for Sync World (between server and client)
	public void sW(BlockPos p, BlockState s) {
		if (!this.level.isClientSide) {
			this.level.sendBlockUpdated(p, s, s, Constants.BlockFlags.BLOCK_UPDATE);

		}
	}

	public void setIO(Direction d, BlockPos p, boolean inputOrOutput, BlockState s) {

		setIOFromDir(d, inputOrOutput);
		sW(p, s);
	}

	public BlockState setVisibleAndIO(Direction d, BlockPos p, boolean inputOrOutput, BlockState s, boolean whetherVisible) {

		setVisibleFromDir(d, whetherVisible);
		s = s.setValue(JunctionGateBlock.dirToProperty(d), whetherVisible);

		setIOFromDir(d, inputOrOutput);
		sW(p,s);
		return s;

	}

	public boolean getVisibleFromDir(Direction d) {
		if (d == Direction.NORTH)
			return northVisible;
		if (d == Direction.EAST)
			return eastVisible;
		if (d == Direction.SOUTH)
			return southVisible;
		if (d == Direction.WEST)
			return westVisible;
		return false;

	}

	public boolean getIOFromDir(Direction d) {
		if (d == Direction.NORTH)
			return northIO;
		if (d == Direction.EAST)
			return eastIO;
		if (d == Direction.SOUTH)
			return southIO;
		if (d == Direction.WEST)
			return westIO;
		return false;

	}

	public BlockState setVisible(Direction d, boolean ifVisible, BlockPos p, BlockState s) {
		setVisibleFromDir(d, true);
		s = s.setValue(JunctionGateBlock.dirToProperty(d), true);
		sW(p, s);
		return s;
	}

	public void setVisibleFromDir(Direction d, boolean ifVisible) {
		if (d == Direction.NORTH)
			northVisible = ifVisible;
		if (d == Direction.EAST)
			eastVisible = ifVisible;
		if (d == Direction.SOUTH)
			southVisible = ifVisible;
		if (d == Direction.WEST)
			westVisible = ifVisible;

	}

	public void setIOFromDir(Direction d, boolean IO) {
		if (d == Direction.NORTH)
			northIO = IO;
		if (d == Direction.EAST)
			eastIO = IO;
		if (d == Direction.SOUTH)
			southIO = IO;
		if (d == Direction.WEST)
			westIO = IO;
	}


	public List<Direction> getOutputDirs() {

		return outputDirs;
	}

	public int getPower() {

		return power;
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
		parentNBT.putInt("power", power);
		CompoundNBT visibles = new CompoundNBT();
		CompoundNBT io = new CompoundNBT();

		visibles.putBoolean("north", northVisible);
		visibles.putBoolean("east", eastVisible);
		visibles.putBoolean("south", southVisible);
		visibles.putBoolean("west", westVisible);

		io.putBoolean("north", northIO);
		io.putBoolean("east", eastIO);
		io.putBoolean("south", southIO);
		io.putBoolean("west", westIO);

		parentNBT.put("visibles", visibles);
		parentNBT.put("io", io);

		return parentNBT;
	}

	@Override
	public void load(BlockState state, CompoundNBT parentNBT) {
		super.load(state, parentNBT);
		power = parentNBT.getInt("power");

		CompoundNBT io = (CompoundNBT) parentNBT.get("io");
		CompoundNBT visibles = (CompoundNBT) parentNBT.get("visibles");

		northVisible = visibles.getBoolean("north");
		eastVisible = visibles.getBoolean("east");
		southVisible = visibles.getBoolean("south");
		westVisible = visibles.getBoolean("west");

		northIO = io.getBoolean("north");
		eastIO = io.getBoolean("east");
		southIO = io.getBoolean("south");
		westIO = io.getBoolean("west");


	}

}
