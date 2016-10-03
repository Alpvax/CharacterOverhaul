package alpvax.characteroverhaul.api.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public final class Config
{
	private static final String CONFIG_VERSION = "1";
	public static Config instance;

	// Gameplay
	public static int numAbilities = 6;
	/*public static boolean spawnWithBook = true;
	public static boolean reuseStencil = true;
	public static boolean craftCastableMaterials = false;
	public static boolean chestsKeepInventory = true;
	public static boolean autosmeltlapis = true;
	public static boolean obsidianAlloy = true;
	public static boolean claycasts = true;
	public static boolean castableBricks = true;
	public static boolean leatherDryingRecipe = true;
	public static boolean gravelFlintRecipe = true;
	public static double oreToIngotRatio = 2;*/

	// Worldgen
	/*public static boolean genSlimeIslands = true;
	public static boolean genIslandsInSuperflat = false;
	public static int slimeIslandsRate = 730; // Every x-th chunk will have a slime island. so 1 = every chunk, 100 = every 100th
	public static int magmaIslandsRate = 100; // Every x-th chunk will have a slime island. so 1 = every chunk, 100 = every 100th
	public static int[] slimeIslandBlacklist = new int[]{-1, 1};
	public static boolean slimeIslandsOnlyGenerateInSurfaceWorlds = true;
	public static boolean genCobalt = true;
	public static int cobaltRate = 16; // max. cobalt per chunk
	public static boolean genArdite = true;
	public static int arditeRate = 16; // max. ardite per chunk*/

	// Clientside configs
	public static boolean renderPotionsInInventory = false;
	public static boolean renderPotionsOnHud = false;
	/*public static boolean extraTooltips = true;
	public static boolean enableForgeBucketModel = true; // enables the forge bucket model by default
	public static boolean dumpTextureMap = false; // requires debug module*/

	/* Config File */
	public static Configuration configFile;

	private static ConfigCategory Gameplay;
	//static ConfigCategory Worldgen;
	private static ConfigCategory ClientSide;

	public static void load(FMLPreInitializationEvent event)
	{
		configFile = new Configuration(event.getSuggestedConfigurationFile(), CONFIG_VERSION, false);

		MinecraftForge.EVENT_BUS.register(instance);

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

			prop = configFile.get(cat, "renderPotionsInInventory", renderPotionsInInventory);
			prop.setComment("If true the vanilla potions rendering will be used in the inventory.");
			renderPotionsInInventory = prop.getBoolean();
			propOrder.add(prop.getName());

			prop = configFile.get(cat, "renderPotionsOnHud", renderPotionsOnHud);
			prop.setComment("If true the vanilla potions rendering will be used on the HUD.");
			renderPotionsOnHud = prop.getBoolean();
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
}
