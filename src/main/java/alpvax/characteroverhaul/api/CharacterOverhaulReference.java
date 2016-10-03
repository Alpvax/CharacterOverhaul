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

	public static final ResourceLocation PERK_REGISTRY_KEY = new ResourceLocation(MOD_ID, "perks");
	public static final ResourceLocation SKILL_REGISTRY_KEY = new ResourceLocation(MOD_ID, "skills");
	public static final ResourceLocation RACE_REGISTRY_KEY = new ResourceLocation(MOD_ID, "races");
	public static final ResourceLocation CHAR_CLASS_REGISTRY_KEY = new ResourceLocation(MOD_ID, "characterclasses");

	public static final int GUI_EFFECTS = 0;
}
