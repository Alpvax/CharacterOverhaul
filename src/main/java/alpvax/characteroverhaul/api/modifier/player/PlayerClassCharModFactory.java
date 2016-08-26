package alpvax.characteroverhaul.api.modifier.player;

import java.util.List;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.RegistryCharModFactory;
import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PlayerClassCharModFactory extends RegistryCharModFactory<PlayerClassHandler, PlayerClass>
{
	public PlayerClassCharModFactory(ResourceLocation key)
	{
		super(CharacterOverhaulReference.MODIFIER_CLASS_KEY, PlayerClass.REGISTRY);
	}

	@Override
	public boolean isValidForCharacter(ICharacter character)
	{
		return character.getAttachedObject() instanceof EntityPlayer;
	}

	@Override
	public PlayerClassHandler createHandler(ICharacter character)
	{
		return new PlayerClassHandler(character);
	}

	@Override
	public List<ICharacterCreationPage> getPagesForGUI()
	{
		// TODO:Race selection GUI
		return null;
	}
}
