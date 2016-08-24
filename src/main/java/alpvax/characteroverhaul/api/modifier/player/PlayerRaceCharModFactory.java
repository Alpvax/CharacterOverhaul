package alpvax.characteroverhaul.api.modifier.player;

import java.util.List;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.RegistryCharModFactory;
import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PlayerRaceCharModFactory extends RegistryCharModFactory<PlayerRaceHandler, PlayerRace>
{
	public PlayerRaceCharModFactory(ResourceLocation key)
	{
		super(CharacterOverhaulReference.MODIFIER_RACE_KEY, PlayerRace.REGISTRY);
	}

	@Override
	public boolean isValidForCharacter(ICharacter character)
	{
		return character.getAttachedObject() instanceof EntityPlayer;
	}

	@Override
	protected PlayerRaceHandler createHandler(ICharacter character)
	{
		return new PlayerRaceHandler(character);
	}

	@Override
	public List<ICharacterCreationPage> getPagesForGUI()
	{
		// TODO:Race selection GUI
		return null;
	}
}
