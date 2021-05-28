//package com.markipol.markisbetterredstone.common.blocks.fancy_block;
//
//import java.util.Objects;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//import com.markipol.markisbetterredstone.Reg;
//import com.markipol.markisbetterredstone.util.Misc;
//
//import net.minecraft.block.BlockState;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.nbt.NBTUtil;
//import net.minecraft.network.NetworkManager;
//import net.minecraft.network.play.server.SUpdateTileEntityPacket;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.tileentity.TileEntityType;
//import net.minecraftforge.client.model.ModelDataManager;
//import net.minecraftforge.client.model.data.IModelData;
//import net.minecraftforge.client.model.data.ModelDataMap;
//import net.minecraftforge.client.model.data.ModelProperty;
//import net.minecraftforge.common.util.Constants;
//
//public class FancyBlockTE extends TileEntity {
//
//	public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
//
//	public BlockState mimic;
//
//	public FancyBlockTE(TileEntityType<? extends TileEntity> type) {
//		super(type);
//
//	}
//
//	public FancyBlockTE() {
//		this(Reg.FANCY_BLOCK_TILE_ENTITY.get());
//	}
//
//	public void setMimic(BlockState mimic) {
//		this.mimic = mimic;
//		setChanged();
//		Misc.log("The wheels are turnt");
//		level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(),
//		        Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
//	}
//
//	public BlockState getMimic() {
//		if (mimic.getBlock() == Reg.FANCY_BLOCK.get()) {
//			return null;
//		} else
//			return mimic;
//	}
//
//	@Override
//	public CompoundNBT getUpdateTag() {
//
//		CompoundNBT nbt = super.getUpdateTag();
//		saveMimic(nbt);
//		return nbt;
//	}
//
//	@Override
//	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
//
//		this.load(state, tag);
//	}
//
//	@Override
//	@Nullable
//	public SUpdateTileEntityPacket getUpdatePacket() {
//
//		CompoundNBT nbt = getUpdateTag();
//		return new SUpdateTileEntityPacket(this.getBlockPos(), 1, nbt);
//	}
//
//	@Override
//	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
//		BlockState oldMimic = mimic;
//		CompoundNBT tag = pkt.getTag();
//		handleUpdateTag(level.getBlockState(pkt.getPos()), tag);
//		if (!Objects.equals(oldMimic, mimic)) {
//			ModelDataManager.requestModelDataRefresh(this);
//			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(),
//			        Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
//		}
//
//		super.onDataPacket(net, pkt);
//	}
//
//	@Nonnull
//	@Override
//	public IModelData getModelData() {
//
//		return new ModelDataMap.Builder().withInitial(MIMIC, mimic).build();
//	}
//
//	@Override
//	public void load(BlockState state, CompoundNBT tag) {
//
//		super.load(state, tag);
//		loadMimic(tag);
//	}
//
//	private void loadMimic(CompoundNBT tag) {
//		if (tag.contains("mimic")) {
//			mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
//		}
//	}
//
//	@Override
//	public CompoundNBT save(CompoundNBT tag) {
//		saveMimic(tag);
//		return super.save(tag);
//	}
//
//	private void saveMimic(CompoundNBT tag) {
//		if (mimic != null) {
//			tag.put("mimic", NBTUtil.writeBlockState(mimic));
//		}
//	}
//
//}
