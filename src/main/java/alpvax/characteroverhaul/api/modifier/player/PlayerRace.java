package alpvax.characteroverhaul.api.modifier.player;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import alpvax.characteroverhaul.api.character.modifier.PerkModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

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
	};

	/**
	 * Change this value in order to allow for more/fewer perks.
	 */
	private static final int MAX_RACE_ID = 0xff;

	public static final FMLControlledNamespacedRegistry<PlayerRace> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(CharacterOverhaulReference.MOD_ID, "races"), PlayerRace.class, STEVE.getRegistryName(), 0, MAX_RACE_ID, true, null, null, null);

	@Override
	public boolean isValidFor(ICharacter character, Map<ResourceLocation, ICharacterModifierHandler<?>> handlers)
	{
		return true;
	}

}
