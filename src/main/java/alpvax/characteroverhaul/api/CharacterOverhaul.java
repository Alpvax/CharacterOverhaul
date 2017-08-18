package alpvax.characteroverhaul.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;

public class CharacterOverhaul
{
	public static final String MOD_ID = "characteroverhaul";
	public static final String MOD_VERSION = "0.1.0";

	public static final String API_ID = MOD_ID + "|API";
	public static final String API_VERSION = "1.0";


	public static final ResourceLocation CAPABILITY_CHARACTER_KEY = new ResourceLocation(MOD_ID, "character");
	public static final ResourceLocation CAPABILITY_AFFECTED_KEY = new ResourceLocation(MOD_ID, "affected");


	private static Logger logger = LogManager.getLogger(MOD_ID);

	public static void log(Level level, String format, Object... data)
	{
		logger.log(level, format, data);
	}

	public static void log(Level level, Throwable ex, String format, Object... data)
	{
		logger.log(level, String.format(format, data), ex);
	}
}
