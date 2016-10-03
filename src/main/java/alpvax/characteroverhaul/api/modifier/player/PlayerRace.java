package alpvax.characteroverhaul.api.modifier.player;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import alpvax.characteroverhaul.api.character.modifier.PerkModifier;
import alpvax.characteroverhaul.api.character.modifier.RegistryCharModHandler;
import alpvax.characteroverhaul.api.skill.SkillInstance.SkillExpModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

public abstract class PlayerRace extends IForgeRegistryEntry.Impl<PlayerRace> implements ICharacterModifier
{
	public PlayerRace(String id)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of player race \"%s\" with no id", toString());
		setRegistryName(id);
	}

	@Override
	public boolean isValidForCharacter(ICharacter character)
	{
		return character.getAttachedObject() instanceof EntityPlayer;
	}

	public static class Handler extends RegistryCharModHandler<PlayerRace>
	{
		public Handler(ICharacter attached)
		{
			super(attached);
		}

		@Override
		public int compareTo(ICharacterModifierHandler<?> arg0)
		{
			return -1;//Should always be first in the list
		}

		@Override
		public ResourceLocation getKey()
		{
			return CharacterOverhaulReference.RACE_REGISTRY_KEY;
		}

		@Override
		protected IForgeRegistry<PlayerRace> getRegistry()//Override to save trying to find and cast
		{
			return REGISTRY;
		}

		@Override
		public boolean setModifier(PlayerRace modifier)
		{
			if(modifier == PlayerRace.STEVE)
			{
				return false;
			}
			return super.setModifier(modifier);
		}

		@Override
		public PlayerRace getDefaultModifier()
		{
			return PlayerRace.STEVE;
		}
	}

	public static final PlayerRace STEVE = new PlayerRace("steve")
	{
		@Override
		public void onAttach(ICharacter character)
		{
		}

		@Override
		public void onDetach(ICharacter character)
		{
		}

		@Override
		public List<PerkModifier> getPerkModifiers()
		{
			return null;
		}

		@Override
		public List<SkillExpModifier> getSkillModifiers()
		{
			return null;
		}
	};

	/**
	 * Change this value in order to allow for more/fewer perks.
	 */
	private static final int MAX_RACE_ID = 0xff;

	public static final IForgeRegistry<PlayerRace> REGISTRY = new RegistryBuilder<PlayerRace>().setName(CharacterOverhaulReference.RACE_REGISTRY_KEY).setType(PlayerRace.class).setIDRange(0, MAX_RACE_ID).create();//STEVE.getRegistryName()
}
