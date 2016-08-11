package alpvax.characteroverhaul.api.modifier.player;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifierHandler;
import net.minecraft.nbt.NBTTagCompound;
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
	public NBTTagCompound serializeNBT()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(ICharacterModifierHandler<?> o)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResourceLocation getKey()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICharacter getCharacter()
	{
		return character;
	}

	@Override
	public boolean setModifier(PlayerClass modifier)
	{
		playerclass = modifier;
		return true;
	}

	@Override
	public void applyModifier()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void removeModifier()
	{
		// TODO Auto-generated method stub

	}

}
