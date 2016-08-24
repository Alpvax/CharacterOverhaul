package alpvax.characteroverhaul.api.modifier.player;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;
import alpvax.characteroverhaul.api.character.modifier.PerkModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

public class PlayerClass extends IForgeRegistryEntry.Impl<PlayerClass> implements ICharacterModifier
{
	public PlayerClass(String id)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of player class \"%s\" with no id", toString());
		setRegistryName(id);
	}

	@Override
	public boolean isValidForCharacter(ICharacter character)
	{
		return character.getAttachedObject() instanceof EntityPlayer;
	}

	@Override
	public void onAttach(ICharacter character)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDetach(ICharacter character)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<PerkModifier> getPerkModifiers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Change this value in order to allow for more/fewer perks.
	 */
	private static final int MAX_CLASS_ID = 0xff;

	public static final FMLControlledNamespacedRegistry<PlayerClass> REGISTRY = PersistentRegistryManager.createRegistry(CharacterOverhaulReference.MODIFIER_CLASS_KEY, PlayerClass.class, null, 0, MAX_CLASS_ID, true, null, null, null);
}
