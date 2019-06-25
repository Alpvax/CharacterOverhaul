package alpvax.mc.characteroverhaul;

import alpvax.mc.characteroverhaul.character.attribute.SortedModifierMap;
import alpvax.mc.characteroverhaul.character.attribute.source.EquipmentAttModSource;
import alpvax.mc.characteroverhaul.character.attribute.source.PotionAttModSource;
import alpvax.mc.characteroverhaul.character.race.RaceManager;
import alpvax.mc.characteroverhaul.command.AttributeCommand;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(CharacterOverhaul.MODID)
public class CharacterOverhaul
{
    public static final String MODID = "characteroverhaul";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public CharacterOverhaul() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        modBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        modBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        modBus.addListener(this::doClientStuff);

        /*TODO: Config
        ModLoadingContext context = ModLoadingContext.get();
        // Register Configs
        context.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        context.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);

         */

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(MODID, "registerAttributeModifierSource", EquipmentAttModSource.factory());
        InterModComms.sendTo(MODID, "registerAttributeModifierSource", PotionAttModSource.factory());
    }

    private void processIMC(final InterModProcessEvent event)
    {
        event.getIMCStream(method -> method.equals("registerAttributeModifierSource")).forEach(imcMessage -> {
            SortedModifierMap.SOURCE_FACTORIES.add(imcMessage.getMessageSupplier());
        });
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
        new AttributeCommand(event.getCommandDispatcher());
        event.getServer().getResourceManager().addReloadListener(new RaceManager());
    }
}
