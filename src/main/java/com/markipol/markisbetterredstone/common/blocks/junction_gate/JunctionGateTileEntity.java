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

	public void sW(BlockPos p) {
		if (!this.level.isClientSide) {
			this.level.sendBlockUpdated(p, this.level.getBlockState(p), this.level.getBlockState(p), Constants.BlockFlags.BLOCK_UPDATE);
		}
	}

	public void setInput(Direction d, BlockPos p) {
//		if (inputDirs.contains(d)) {
//			sW(p);
//			return;
//		}
		if (outputDirs.contains(d)) {
			outputDirs.remove(d);
			inputDirs.add(d);
			sW(p);
			return;
		}
		inputDirs.add(d);
		sW(p);
	}

	public void setOutput(Direction d, BlockPos p) {
//		if (outputDirs.contains(d)) {
//			sW(p);
//			return;
//		}
		if (inputDirs.contains(d)) {
			inputDirs.remove(d);
			outputDirs.add(d);
			sW(p);
			return;
		}
		outputDirs.add(d);
		sW(p);
	}

	public void setEmpty(Direction d, BlockPos p) {

		outputDirs.remove(d);
		sW(p);
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
		parentNBT.putBoolean("northInput", inputDirs.contains(Direction.NORTH));
		parentNBT.putBoolean("eastInput", inputDirs.contains(Direction.EAST));
		parentNBT.putBoolean("southInput", inputDirs.contains(Direction.SOUTH));
		parentNBT.putBoolean("westInput", inputDirs.contains(Direction.WEST));

		parentNBT.putBoolean("northOutput", outputDirs.contains(Direction.NORTH));
		parentNBT.putBoolean("eastOutput", outputDirs.contains(Direction.EAST));
		parentNBT.putBoolean("southOutput", outputDirs.contains(Direction.SOUTH));
		parentNBT.putBoolean("westOutput", outputDirs.contains(Direction.WEST));

		return parentNBT;
	}

	@Override
	public void load(BlockState state, CompoundNBT parentNBT) {
		super.load(state, parentNBT);
		power = parentNBT.getInt("power");
		// parentNBT.getBoolean("northInput") ? inputDirs.add(Direction.NORTH) :
		// inputDirs.remove(Direction.NORTH);
		
		//North
		if (parentNBT.getBoolean("northInput") && !parentNBT.getBoolean("northOutput")) {
			if (!inputDirs.contains(Direction.NORTH)) {
				inputDirs.add(Direction.NORTH);
			}
			outputDirs.remove(Direction.NORTH);
		}
		if (!parentNBT.getBoolean("northInput") && parentNBT.getBoolean("northOutput")) {
			if (!outputDirs.contains(Direction.NORTH)) {
				outputDirs.add(Direction.NORTH);
			}
			inputDirs.remove(Direction.NORTH);
		}
		//East
		if (parentNBT.getBoolean("eastInput") && !parentNBT.getBoolean("eastOutput")) {
			if (!inputDirs.contains(Direction.EAST)) {
				inputDirs.add(Direction.EAST);
			}
			outputDirs.remove(Direction.EAST);
		}
		if (!parentNBT.getBoolean("eastInput") && parentNBT.getBoolean("eastOutput")) {
			if (!outputDirs.contains(Direction.EAST)) {
				outputDirs.add(Direction.EAST);
			}
			inputDirs.remove(Direction.EAST);
		}
		//South
		if (parentNBT.getBoolean("southInput") && !parentNBT.getBoolean("southOutput")) {
			if (!inputDirs.contains(Direction.SOUTH)) {
				inputDirs.add(Direction.SOUTH);
			}
			outputDirs.remove(Direction.SOUTH);
		}
		if (!parentNBT.getBoolean("southInput") && parentNBT.getBoolean("southOutput")) {
			if (!outputDirs.contains(Direction.SOUTH)) {
				outputDirs.add(Direction.SOUTH);
			}
			inputDirs.remove(Direction.SOUTH);
		}
		//West
		if (parentNBT.getBoolean("westInput") && !parentNBT.getBoolean("westOutput")) {
			if (!inputDirs.contains(Direction.WEST)) {
				inputDirs.add(Direction.WEST);
			}
			outputDirs.remove(Direction.WEST);
		}
		if (!parentNBT.getBoolean("westInput") && parentNBT.getBoolean("westOutput")) {
			if (!outputDirs.contains(Direction.WEST)) {
				outputDirs.add(Direction.WEST);
			}
			inputDirs.remove(Direction.WEST);
		}
		
		
//		//Inputs
//
//		// North
//		if (parentNBT.getBoolean("northInput")) {
//			if (!inputDirs.contains(Direction.NORTH))
//				inputDirs.add(Direction.NORTH);
//		} else
//			inputDirs.remove(Direction.NORTH);
//
//		// East
//		if (parentNBT.getBoolean("eastInput"))
//			if (!inputDirs.contains(Direction.EAST))
//				inputDirs.add(Direction.EAST);
//			else
//				inputDirs.remove(Direction.EAST);
//
//		// South
//		if (parentNBT.getBoolean("southInput"))
//			if (!inputDirs.contains(Direction.SOUTH))
//				inputDirs.add(Direction.SOUTH);
//			else
//				inputDirs.remove(Direction.SOUTH);
//
//		// West
//		if (parentNBT.getBoolean("westInput"))
//			if (!inputDirs.contains(Direction.WEST))
//				inputDirs.add(Direction.WEST);
//			else
//				inputDirs.remove(Direction.WEST);
//		
//		//Outputs
//		
//		// North
//		if (parentNBT.getBoolean("northOutput")) {
//			if (!outputDirs.contains(Direction.NORTH))
//				outputDirs.add(Direction.NORTH);
//		} else
//			outputDirs.remove(Direction.NORTH);
//
//		// East
//		if (parentNBT.getBoolean("eastOutput"))
//			if (!outputDirs.contains(Direction.EAST))
//				outputDirs.add(Direction.EAST);
//			else
//				outputDirs.remove(Direction.EAST);
//
//		// South
//		if (parentNBT.getBoolean("southOutput"))
//			if (!outputDirs.contains(Direction.SOUTH))
//				outputDirs.add(Direction.SOUTH);
//			else
//				outputDirs.remove(Direction.SOUTH);
//
//		// West
//		if (parentNBT.getBoolean("westOutput"))
//			if (!outputDirs.contains(Direction.WEST))
//				outputDirs.add(Direction.WEST);
//			else
//				outputDirs.remove(Direction.WEST);
//		
//
//		

	}

}
