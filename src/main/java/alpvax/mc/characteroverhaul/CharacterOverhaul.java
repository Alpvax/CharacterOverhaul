package alpvax.mc.characteroverhaul;

import alpvax.mc.characteroverhaul.character.attribute.SortedModifierMap;
import alpvax.mc.characteroverhaul.character.attribute.source.EquipmentAttModSource;
import alpvax.mc.characteroverhaul.character.attribute.source.PotionAttModSource;
import alpvax.mc.characteroverhaul.character.race.RaceManager;
import alpvax.mc.characteroverhaul.client.ClientProxy;
import alpvax.mc.characteroverhaul.command.AttributeCommand;
import alpvax.mc.characteroverhaul.proxy.IProxy;
import alpvax.mc.characteroverhaul.proxy.ServerProxy;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(CharacterOverhaul.MODID)
public class CharacterOverhaul
{
    public static final String MODID = "characteroverhaul";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

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
    }
    @SubscribeEvent
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        event.getServer().getResourceManager().addReloadListener(new RaceManager());
    }

    private static Method setPose = ObfuscationReflectionHelper.findMethod(Entity.class, "func_213301_b", Pose.class);
    static {
        setPose.setAccessible(true);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                if (ClientProxy.prone.isKeyDown()) {
                    try {
                        setPose.invoke(event.player, Pose.SWIMMING);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOGGER.error("Error setting player prone: " + event.player.getDisplayNameAndUUID(), e);
                    }
                }
            });
        }
    }
}
