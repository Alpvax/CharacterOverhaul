package alpvax.characteroverhaul.core;

import static alpvax.characteroverhaul.api.CharacterOverhaulReference.MOD_ID;
import static alpvax.characteroverhaul.api.CharacterOverhaulReference.VERSION;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.Level;

import alpvax.characteroverhaul.api.character.modifier.CharacterModifierFactory;
import alpvax.characteroverhaul.api.character.modifier.RegistryCharModFactory;
import alpvax.characteroverhaul.api.modifier.player.PlayerRace;
import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.api.skill.Skill;
import alpvax.characteroverhaul.capabilities.CapabilityCharacterHandler;
import alpvax.characteroverhaul.command.CharacterCommand;
import alpvax.characteroverhaul.core.proxy.CommonProxy;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

@Mod(modid = MOD_ID, version = VERSION, guiFactory = "alpvax.characteroverhaul.client.COGuiFactory")
public class CharacterOverhaul
{
	@SidedProxy(
			clientSide = "alpvax.characteroverhaul.core.proxy.ClientProxy",
			serverSide = "alpvax.characteroverhaul.core.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance(MOD_ID)
	public static CharacterOverhaul instance;

	@Metadata(MOD_ID)
	public static ModMetadata meta;

	public static Configuration config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		meta.name = I18n.format("mod." + MOD_ID + ".name");
		meta.description = I18n.format("mod." + MOD_ID + ".description");
		meta.authorList.add("Alpvax");
		meta.autogenerated = false;

		proxy.registerPre();

		CapabilityCharacterHandler.register();

		GameRegistry.register(PlayerRace.STEVE);

		MinecraftForge.EVENT_BUS.register(instance);
		MinecraftForge.EVENT_BUS.register(new CharacterOverhaulHooks());
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CharacterCommand());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		String form = "Detected %d registered %s:%n";
		//Ensure Skill registry is initialised, even if no skills were registered.
		FMLLog.log("Character Overhaul", Level.INFO, getRegistryLogText(form, Skill.REGISTRY, "skill", "skills"));
		//Ensure Perk registry is initialised, even if no perks were registered.
		FMLLog.log("Character Overhaul", Level.INFO, getRegistryLogText(form, Perk.REGISTRY, "perk", "perks"));
		//Ensure CharacterModifierFactory registry is initialised, even if no modifier factories were registered.
		//Also does the same for any registry factories.
		List<String> registryModifiers = new ArrayList<>();
		FMLLog.log("Character Overhaul", Level.INFO, getRegistryLogText(form, CharacterModifierFactory.REGISTRY, "factory", "factories", (CharacterModifierFactory<?> factory) -> {
			if(factory instanceof RegistryCharModFactory<?, ?>)
			{
				String s = factory.getRegistryName().getResourcePath();
				registryModifiers.add(getRegistryLogText(form, ((RegistryCharModFactory<?, ?>)factory).registry(), s, s));
			}
		}));
		registryModifiers.forEach((String s) -> FMLLog.log("Character Overhaul", Level.INFO, s));

	}

	private <T extends IForgeRegistryEntry<T>> String getRegistryLogText(String form, FMLControlledNamespacedRegistry<T> registry, String singularArg, String multipleArg)
	{
		return getRegistryLogText(form, registry, singularArg, multipleArg, null);
	}

	private <T extends IForgeRegistryEntry<T>> String getRegistryLogText(String form, FMLControlledNamespacedRegistry<T> registry, String singularArg, String multipleArg, Consumer<T> forEach)
	{
		List<T> values = registry.getValues();
		int num = values.size();
		StringBuilder sb = new StringBuilder(String.format(form, num, num == 1 ? singularArg : multipleArg));
		for(T t : values)
		{
			sb.append("\n").append(t.getRegistryName().toString());
			if(forEach != null)
			{
				forEach.accept(t);
			}
		}
		return sb.toString();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equals(MOD_ID))
		{
			syncConfig();
		}
	}

	public void syncConfig()
	{
	}
}
