package alpvax.characteroverhaul.api.config;

import java.util.List;

import com.google.common.collect.Lists;

import alpvax.characteroverhaul.core.CharacterOverhaul;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// @Config(modid = CharacterOverhaul.MOD_ID)
public class CharacterConfig
{
	private static final String CONFIG_VERSION = "1";

	// Gameplay
	@Config.Comment("The number of abilities allowed on the hotbar")
	public static int numAbilities = 6;

	public static Client client = new Client();

	public static class Client
	{
		public boolean renderPotionsInInventory = true;
		public boolean renderPotionsOnHud = false;
	}

	/* Config File */
	public static Configuration configFile;

	private static ConfigCategory Gameplay;
	//static ConfigCategory Worldgen;
	private static ConfigCategory ClientSide;

	public static void load(FMLPreInitializationEvent event)
	{
		configFile = new Configuration(event.getSuggestedConfigurationFile(), CONFIG_VERSION, false);

		//MinecraftForge.EVENT_BUS.register(instance);

		syncConfig();
	}


	public static boolean syncConfig()
	{
		Property prop;
		// Gameplay
		{
			String cat = "gameplay";
			List<String> propOrder = Lists.newArrayList();
			Gameplay = configFile.getCategory(cat);

			prop = configFile.get(cat, "numAbilities", numAbilities);
			prop.setComment("The number of abilities allowed on the hotbar");
			numAbilities = prop.getInt();

			/*propOrder.add(prop.getName());
			prop = configFile.get(cat, "addLeatherDryingRecipe", leatherDryingRecipe);
			prop.setComment("Adds a recipe that allows you to get leather from drying cooked meat");
			leatherDryingRecipe = prop.getBoolean();
			prop.requiresMcRestart();
			propOrder.add(prop.getName());*/

			Gameplay.setPropertyOrder(propOrder);
		}
		/*//Worldgen
		{
			String cat = "worldgen";
			List<String> propOrder = Lists.newArrayList();
			Worldgen = configFile.getCategory(cat);

			// Slime Islands
			prop = configFile.get(cat, "generateSlimeIslands", genSlimeIslands);
			prop.setComment("If true slime islands will generate");
			genSlimeIslands = prop.getBoolean();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "generateIslandsInSuperflat", genIslandsInSuperflat);
			prop.setComment("If true slime islands generate in superflat worlds");
			genIslandsInSuperflat = prop.getBoolean();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "slimeIslandRate", slimeIslandsRate);
			prop.setComment("One in every X chunks will contain a slime island");
			slimeIslandsRate = prop.getInt();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "magmaIslandRate", magmaIslandsRate);
			prop.setComment("One in every X chunks will contain a magma island in the nether");
			magmaIslandsRate = prop.getInt();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "slimeIslandBlacklist", slimeIslandBlacklist);
			prop.setComment("Prevents generation of slime islands in the listed dimensions");
			slimeIslandBlacklist = prop.getIntList();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "slimeIslandsOnlyGenerateInSurfaceWorlds", slimeIslandsOnlyGenerateInSurfaceWorlds);
			prop.setComment("If true, slime islands wont generate in dimensions which aren't of type surface. This means they wont generate in modded cave dimensions like the deep dark.");
			slimeIslandsOnlyGenerateInSurfaceWorlds = prop.getBoolean();
			propOrder.add(prop.getName());

			// Nether ore generation
			prop = configFile.get(cat, "genCobalt", genCobalt);
			prop.setComment("If true, cobalt ore will generate in the nether");
			genCobalt = prop.getBoolean();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "genArdite", genArdite);
			prop.setComment("If true, ardite ore will generate in the nether");
			genArdite = prop.getBoolean();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "cobaltRate", cobaltRate);
			prop.setComment("Approx Ores per chunk");
			cobaltRate = prop.getInt();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "arditeRate", arditeRate);
			arditeRate = prop.getInt();
			propOrder.add(prop.getName());

			Worldgen.setPropertyOrder(propOrder);
		}*/
		//Clientside
		{
			String cat = "clientside";
			List<String> propOrder = Lists.newArrayList();
			ClientSide = configFile.getCategory(cat);

			prop = configFile.get(cat, "renderPotionsInInventory", client.renderPotionsInInventory);
			prop.setComment("If true the vanilla potions rendering will be used in the inventory.");
			client.renderPotionsInInventory = prop.getBoolean();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "renderPotionsOnHud", client.renderPotionsOnHud);
			prop.setComment("If true the vanilla potions rendering will be used on the HUD.");
			client.renderPotionsOnHud = prop.getBoolean();
			propOrder.add(prop.getName());

			ClientSide.setPropertyOrder(propOrder);
		}

		//save changes if any
		boolean changed = false;
		if(configFile.hasChanged())
		{
			configFile.save();
			changed = true;
		}
		return changed;
	}

	@EventBusSubscriber
	static class ConfigHandler
	{
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if(event.getModID().equals(CharacterOverhaul.MOD_ID))
			{
				ConfigManager.load(CharacterOverhaul.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
