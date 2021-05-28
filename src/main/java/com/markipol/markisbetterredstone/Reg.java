package com.markipol.markisbetterredstone;

import com.markipol.markisbetterredstone.common.blocks.CornerGateBlock;
import com.markipol.markisbetterredstone.common.blocks.colored_lamp.ColoredLampBlock;
import com.markipol.markisbetterredstone.common.blocks.colored_lamp.ColoredLampTileEntity;

import com.markipol.markisbetterredstone.common.blocks.junction_gate.JunctionGateBlock;
import com.markipol.markisbetterredstone.common.blocks.junction_gate.JunctionGateTileEntity;
import com.markipol.markisbetterredstone.common.blocks.magic_block.MagicBlock;
import com.markipol.markisbetterredstone.common.blocks.magic_block.MagicBlockTileEntity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Reg {
	// Who registers the registers?
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
	        MarkisBetterRedstone.id);

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
	        MarkisBetterRedstone.id);
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister
	        .create(ForgeRegistries.TILE_ENTITIES, MarkisBetterRedstone.id);
	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister
	        .create(ForgeRegistries.CONTAINERS, MarkisBetterRedstone.id);
	// Blocks
	public static final RegistryObject<Block> CORNER_GATE_BLOCK = BLOCKS.register("corner_gate",
	        () -> new CornerGateBlock(AbstractBlock.Properties.of(Material.STONE).noOcclusion()));
	public static final RegistryObject<Block> COLORED_LAMP_BLOCK = BLOCKS.register("colored_lamp",
	        () -> new ColoredLampBlock());
	public static final RegistryObject<Block> JUNCTION_GATE_BLOCK = BLOCKS.register("junction_gate",
	        () -> new JunctionGateBlock(AbstractBlock.Properties.of(Material.WOOL).harvestTool(ToolType.PICKAXE)
	                .harvestLevel(-1).sound(SoundType.ANVIL).noOcclusion()));
	public static final RegistryObject<Block> MAGIC_BLOCK = BLOCKS.register("magic_block", () -> new MagicBlock());
	
//  That's 3 hours I'm never getting back...
//	public static final RegistryObject<Block> FANCY_BLOCK = BLOCKS.register("fancy_block", () -> new FancyBlock());
	
	// Items
	public static final RegistryObject<Item> CORNER_GATE_ITEM = ITEMS.register("corner_gate",
	        () -> new BlockItem(CORNER_GATE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
	public static final RegistryObject<Item> COLORED_LAMP_ITEM = ITEMS.register("colored_lamp",
	        () -> new BlockItem(COLORED_LAMP_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
	public static final RegistryObject<Item> MAGIC_BLOCK_ITEM = ITEMS.register("magic_block",
	        () -> new BlockItem(MAGIC_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
//	public static final RegistryObject<Item> FANCY_BLOCK_ITEM = ITEMS.register("fancy_block", 
//			() -> new BlockItem(FANCY_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

	public static final RegistryObject<Item> JUNCTION_GATE_ITEM = ITEMS.register("junction_gate",
	        () -> new BlockItem(JUNCTION_GATE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
	// Tile Entities
	public static final RegistryObject<TileEntityType<ColoredLampTileEntity>> COLORED_LAMP_TILE_ENTITY = TILE_ENTITY_TYPES
	        .register("colored_lamp",
	                () -> TileEntityType.Builder.of(ColoredLampTileEntity::new, COLORED_LAMP_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<JunctionGateTileEntity>> JUNCTION_GATE_TILE_ENTITY = TILE_ENTITY_TYPES
	        .register("junction_gate", () -> TileEntityType.Builder
	                .of(JunctionGateTileEntity::new, JUNCTION_GATE_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<MagicBlockTileEntity>> MAGIC_BLOCK_TILE_ENTITY = TILE_ENTITY_TYPES
	        .register("magic_block",
	                () -> TileEntityType.Builder.of(MagicBlockTileEntity::new, MAGIC_BLOCK.get()).build(null));
//
//	public static final RegistryObject<TileEntityType<FancyBlockTE>> FANCY_BLOCK_TILE_ENTITY = TILE_ENTITY_TYPES
//			.register("fancy_block", () -> TileEntityType.Builder.of(FancyBlockTE::new, FANCY_BLOCK.get()).build(null));

}
