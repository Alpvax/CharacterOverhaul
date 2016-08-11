package alpvax.characteroverhaul.api.settings;

public class Settings
{
	//TODO: implement config
	private static Config defaults = new Config(null)
	{
		int numAbilities = 6;
	};
	public static void loadConfig()
	{
		
	}

	public static Config getCurrentConfig()
	{
		return defaults;
	}
}
