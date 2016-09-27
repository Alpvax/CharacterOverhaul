package alpvax.characteroverhaul.capabilities;

import alpvax.characteroverhaul.api.character.AffectedBase;
import alpvax.characteroverhaul.api.character.IAffected;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class AffectedCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
{
	private final IAffected affected;

	public AffectedCapabilityProvider(ICapabilityProvider attachTo)
	{
		this(new AffectedBase(attachTo));
	}

	public AffectedCapabilityProvider(IAffected handler)
	{
		affected = handler;
	}

	@SuppressWarnings("unchecked")
	protected <T extends IAffected> T get()
	{
		return (T)affected;
	}


	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == IAffected.CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return hasCapability(capability, facing) ? (T)get() : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound)IAffected.CAPABILITY.writeNBT(get(), null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		IAffected.CAPABILITY.readNBT(get(), null, nbt);
	}

}
