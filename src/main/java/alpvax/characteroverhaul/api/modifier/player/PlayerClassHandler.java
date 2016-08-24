package alpvax.characteroverhaul.api.modifier.player;

import java.util.List;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import alpvax.characteroverhaul.api.character.modifier.PerkModifier;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

public class PlayerClassHandler implements ICharacterModifierHandler<PlayerClass>
{
	private final ICharacter character;
	private PlayerClass playerclass = null;

	public PlayerClassHandler(ICharacter attached)
	{
		character = attached;
	}

	@Override
	public NBTTagString serializeNBT()
	{
		return new NBTTagString(playerclass.getRegistryName().toString());
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		setModifier(PlayerClass.REGISTRY.getObject(new ResourceLocation(((NBTTagString)nbt).getString())));
	}

	@Override
	public ResourceLocation getKey()
	{
		return CharacterOverhaulReference.MODIFIER_CLASS_KEY;
	}

	@Override
	public ICharacter getCharacter()
	{
		return character;
	}

	@Override
	public boolean setModifier(PlayerClass modifier)
	{
		if(modifier != null)
		{
			return false;
		}
		playerclass = modifier;
		return true;
	}

	@Override
	public List<PerkModifier> getPerkModifiers()
	{
		return playerclass.getPerkModifiers();
	}

}
