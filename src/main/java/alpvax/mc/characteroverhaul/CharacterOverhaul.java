package alpvax.mc.characteroverhaul;

import alpvax.mc.characteroverhaul.character.capability.CharacterCapability;
import alpvax.mc.characteroverhaul.network.PacketHandler;
import alpvax.mc.characteroverhaul.util.attribute.SortedModifierMap;
import alpvax.mc.characteroverhaul.util.attribute.source.EquipmentAttModSource;
import alpvax.mc.characteroverhaul.util.attribute.source.PotionAttModSource;
import net.minecraft.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
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
        PacketHandler.register();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        CharacterCapability.register();
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
        InterModComms.sendTo(MODID, "registerAttributeModifierSource", EquipmentAttModSource.factory());
        InterModComms.sendTo(MODID, "registerAttributeModifierSource", PotionAttModSource.factory());
    }

    private void processIMC(final InterModProcessEvent event)
    {
        event.getIMCStream().forEach(imcMessage -> {
            switch (imcMessage.getMethod()) {
                case "registerAttributeModifierSource":
                    SortedModifierMap.SOURCE_FACTORIES.add(imcMessage.getMessageSupplier());
                    break;
                case "registerCharacterModifierType":
                    //ICharacterModifierType.registerType(imcMessage.<Pair<String, ICharacterModifierType>>getMessageSupplier().get());
                    break;
            }
        });
    }
}
