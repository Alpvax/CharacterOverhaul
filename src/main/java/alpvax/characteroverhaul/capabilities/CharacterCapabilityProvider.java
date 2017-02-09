package alpvax.characteroverhaul.capabilities;

import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CharacterCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
{
	private final ICharacter character;

	public CharacterCapabilityProvider(ICapabilityProvider attachTo)
	{
		this(new CharacterBase(attachTo));
	}

	public CharacterCapabilityProvider(ICharacter handler)
	{
		character = handler;
	}


	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ICharacter.CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return hasCapability(capability, facing) ? ICharacter.CAPABILITY.cast(character) : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return character.serializeNBT();
		//return (NBTTagCompound)ICharacter.CAPABILITY.writeNBT(get(), null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		character.deserializeNBT(nbt);
		//ICharacter.CAPABILITY.readNBT(get(), null, nbt);
	}

}
