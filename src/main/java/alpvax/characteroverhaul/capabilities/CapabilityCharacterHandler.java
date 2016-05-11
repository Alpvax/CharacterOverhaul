package alpvax.characteroverhaul.capabilities;

import java.util.concurrent.Callable;

import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.character.CharacterBase;
import alpvax.characteroverhaul.character.ICharacter;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants.NBT;

public class CapabilityCharacterHandler
{
	private static final class Keys
	{
		private static final String PERKS = "Perks";
	}


	@CapabilityInject(ICharacter.class)
	public static Capability<ICharacter> CHARACTER_CAPABILITY = null;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(ICharacter.class, new Capability.IStorage<ICharacter>()
		{
			@Override
			public NBTBase writeNBT(Capability<ICharacter> capability, ICharacter instance, EnumFacing side)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				NBTTagCompound perks = new NBTTagCompound();
				for(Perk perk : Perk.registry.getValues())
				{
					int l = instance.getPerkLevel(perk);
					if(l != 0)
					{
						perks.setInteger(perk.getRegistryName().toString(), l);
					}
				}
				if(perks.getSize() > 0)
				{
					nbt.setTag(Keys.PERKS, perks);
				}
				return nbt;
			}

			@Override
			public void readNBT(Capability<ICharacter> capability, ICharacter instance, EnumFacing side, NBTBase base)
			{
				NBTTagCompound nbt = (NBTTagCompound)base;
				if(nbt.hasKey(Keys.PERKS, NBT.TAG_COMPOUND))
				{
					NBTTagCompound perks = nbt.getCompoundTag(Keys.PERKS);
					for(String s : perks.getKeySet())
					{
						Perk perk = Perk.registry.getObject(new ResourceLocation(s));
						instance.setPerkLevel(perk, perks.getInteger(s));
					}
				}
			}
		}, new Callable<ICharacter>()
		{
			@Override
			public ICharacter call() throws Exception
			{
				return new CharacterBase()
				{
					@Override
					public <T extends ICapabilityProvider> T getAttachedObject()
					{
						return null;
					}

					@Override
					public AbstractAttributeMap getAttributeMap()
					{
						return null;
					}
				};
			}
		});
	}
}
