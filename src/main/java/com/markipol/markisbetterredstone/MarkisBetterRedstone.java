package com.markipol.markisbetterredstone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.markipol.markisbetterredstone.common.blocks.colored_lamp.LampColor;

import com.markipol.markisbetterredstone.common.blocks.junction_gate.JunctionGateColor;
import com.markipol.markisbetterredstone.common.blocks.magic_block.MagicBlockTER;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("markisbetterredstone")
public class MarkisBetterRedstone
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String id = "markisbetterredstone";

    public MarkisBetterRedstone() {
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::onColorHandlerEvent);
        bus.addListener(this::clientSetup);
        
        Reg.BLOCKS.register(bus);
        Reg.ITEMS.register(bus);
        Reg.TILE_ENTITY_TYPES.register(bus);
        Reg.CONTAINER_TYPES.register(bus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void clientSetup(final FMLClientSetupEvent event) {
    	RenderTypeLookup.setRenderLayer(Reg.COLORED_LAMP_BLOCK.get(), RenderType.cutoutMipped());
    	ClientRegistry.bindTileEntityRenderer(Reg.MAGIC_BLOCK_TILE_ENTITY.get(), MagicBlockTER::new);
    	

    	RenderTypeLookup.setRenderLayer(Reg.JUNCTION_GATE_BLOCK.get(), RenderType.translucent());
//    	RenderTypeLookup.setRenderLayer(Reg.FANCY_BLOCK.get(), (RenderType) -> true);
    }
    
    
    public void onColorHandlerEvent(ColorHandlerEvent.Block event) {
    	event.getBlockColors().register(new LampColor(), Reg.COLORED_LAMP_BLOCK.get());
    	event.getBlockColors().register(new JunctionGateColor(), Reg.JUNCTION_GATE_BLOCK.get());
    	
    }
    
    public void onModelRegistryEvent (ModelRegistryEvent event) {
//    	ModelLoaderRegistry.registerLoader(new ResourceLocation(id, "fancy_loader"), new FancyModelLoader());
    }

    
    @SuppressWarnings("deprecation")
    @SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
    	if (!event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
    		return;
    	}
    	event.addSprite(MagicBlockTER.TEXTURE);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
