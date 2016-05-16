package alpvax.characteroverhaul.capabilities;

import java.util.concurrent.Callable;

import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.perk.Perk;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants.NBT;

public class CapabilityCharacterHandler
{
	private static final class Keys
	{
		private static final String PERKS = "Perks";
		private static final String MODIFIERS = "Modifiers";
		private static final String UUID_MOST = "IDMost";
		private static final String UUID_LEAST = "IDLeast";
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
				//Save Perks
				NBTTagCompound perks = new NBTTagCompound();
				for(Perk perk : Perk.REGISTRY.getValues())
				{
					int l = instance.getPerkLevel(perk);
					if(l != 0)
					{
						perks.setInteger(perk.getRegistryName().toString(), l);
					}
				}
				if(!perks.hasNoTags())
				{
					nbt.setTag(Keys.PERKS, perks);
				}
				/*Save Modifiers
				NBTTagList modifiers = new NBTTagList();
				for(ICharacterModifier modifier : instance.getModifiers())
				{
					if(modifier instanceof ICharacterModifierExtended)
					{
						ICharacterModifierExtended m = (ICharacterModifierExtended)modifier;
						if(m.shouldSaveNBTToCharacter())
						{
							NBTTagCompound mnbt = m.serializeNBT();
							UUID id = m.getID();
							mnbt.setLong(Keys.UUID_MOST, id.getMostSignificantBits());
							mnbt.setLong(Keys.UUID_LEAST, id.getLeastSignificantBits());
							modifiers.appendTag(mnbt);
						}
					}
				}
				if(!modifiers.hasNoTags())
				{
					nbt.setTag(Keys.MODIFIERS, modifiers);
				}*/
				return nbt;
			}

			@Override
			public void readNBT(Capability<ICharacter> capability, ICharacter instance, EnumFacing side, NBTBase base)
			{
				NBTTagCompound nbt = (NBTTagCompound)base;
				//Load Perks
				if(nbt.hasKey(Keys.PERKS, NBT.TAG_COMPOUND))
				{
					NBTTagCompound perks = nbt.getCompoundTag(Keys.PERKS);
					for(String s : perks.getKeySet())
					{
						Perk perk = Perk.REGISTRY.getObject(new ResourceLocation(s));
						instance.setPerkLevel(perk, perks.getInteger(s));
					}
				}
				/*Load modifiers
				if(nbt.hasKey(Keys.MODIFIERS, NBT.TAG_LIST))
				{
					NBTTagList modifiers = nbt.getTagList(Keys.MODIFIERS, NBT.TAG_COMPOUND);
					for(int i = 0; i < modifiers.tagCount(); i++)
					{
						NBTTagCompound mnbt = modifiers.getCompoundTagAt(i);
						UUID id = new UUID(mnbt.getLong(Keys.UUID_MOST), mnbt.getLong(Keys.UUID_LEAST));
						ICharacterModifierExtended modifier = (ICharacterModifierExtended)instance.getModifier(id);
						if(!modifier.shouldSaveNBTToCharacter())
						{
							FMLLog.warning("CharacterModifier with id %s has an data saved with the Character, but shouldn't be saving data to the character.", id);
						}
						modifier.deserializeNBT(mnbt);
					}
				}*/
			}
		}, new Callable<ICharacter>()
		{
			@Override
			public ICharacter call() throws Exception
			{
				return new CharacterBase(null);
			}
		});
	}
}
