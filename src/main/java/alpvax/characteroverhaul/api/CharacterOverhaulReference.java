package alpvax.characteroverhaul.api;

import net.minecraft.util.ResourceLocation;

public class CharacterOverhaulReference
{
	private static final String MOD_ID = "characteroverhaul";
	public static final String MOD_VERSION = "0.1.0";

	public static final String API_ID = MOD_ID + "|API";
	public static final String API_VERSION = "1.0";


	public static final ResourceLocation CAPABILITY_CHARACTER_KEY = new ResourceLocation(MOD_ID, "character");
	public static final ResourceLocation CAPABILITY_AFFECTED_KEY = new ResourceLocation(MOD_ID, "affected");
}
