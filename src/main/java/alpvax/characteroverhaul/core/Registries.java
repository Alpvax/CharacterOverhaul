package alpvax.characteroverhaul.core;

import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.api.perk.PerkTree;
import alpvax.characteroverhaul.api.skill.Skill;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

@EventBusSubscriber
public class Registries
{
	/*public static final ResourceLocation RACE_REGISTRY_KEY = new ResourceLocation(CharacterOverhaul.MOD_ID, "races");
	public static final ResourceLocation CHAR_CLASS_REGISTRY_KEY = new ResourceLocation(CharacterOverhaul.MOD_ID, "characterclasses");*/

	@SubscribeEvent
	public static void createRegistries(RegistryEvent.NewRegistry event)
	{
		new RegistryBuilder<Perk>().setName(new ResourceLocation(CharacterOverhaul.MOD_ID, "perks")).setType(Perk.class).setIDRange(0, 0x400).create();
		new RegistryBuilder<PerkTree>().setName(new ResourceLocation(CharacterOverhaul.MOD_ID, "perktrees")).setType(PerkTree.class).setIDRange(0, 0xff).create();
		new RegistryBuilder<Skill>().setName(new ResourceLocation(CharacterOverhaul.MOD_ID, "skills")).setType(Skill.class).setIDRange(0, 0xff).create();
	}

	/*@SubscribeEvent
	public static void register(RegistryEvent.Register<PlayerRace> event)
	{
		event.getRegistry().register(PlayerRace.STEVE);
	}*/
}
