package alpvax.characteroverhaul.capabilities;

import java.util.concurrent.Callable;

import alpvax.characteroverhaul.character.ICharacter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityCharacterHandler
{
	@CapabilityInject(ICharacter.class)
	public static Capability<ICharacter> CHARACTER_CAPABILITY = null;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(ICharacter.class, new Capability.IStorage<ICharacter>()
		{
			@Override
			public NBTBase writeNBT(Capability<ICharacter> capability, ICharacter instance, EnumFacing side)
			{
				return null;
			}

			@Override
			public void readNBT(Capability<ICharacter> capability, ICharacter instance, EnumFacing side, NBTBase base)
			{}
		}, new Callable<ICharacter>()
		{
			@Override
			public ICharacter call() throws Exception
			{
				return null;//TODO:new SimpleCharacter(null);
			}
		});
	}
}
