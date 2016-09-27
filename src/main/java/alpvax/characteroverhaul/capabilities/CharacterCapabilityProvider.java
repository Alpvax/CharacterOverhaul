package alpvax.characteroverhaul.capabilities;

import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CharacterCapabilityProvider extends AffectedCapabilityProvider
{
	public CharacterCapabilityProvider(ICapabilityProvider attachTo)
	{
		this(new CharacterBase(attachTo));
	}

	public CharacterCapabilityProvider(ICharacter handler)
	{
		super(handler);
	}


	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ICharacter.CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return hasCapability(capability, facing) ? get() : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound)ICharacter.CAPABILITY.writeNBT(get(), null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		ICharacter.CAPABILITY.readNBT(get(), null, nbt);
	}

}
