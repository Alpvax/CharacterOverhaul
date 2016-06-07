package alpvax.characteroverhaul.capabilities;

import java.util.concurrent.Callable;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.ability.AbilityInstance;
import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.perk.Perk;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLLog;

public class CapabilityCharacterHandler
{
	private static final class Keys
	{
		private static final String PERKS = "Perks";
		private static final String ABILITIES = "Abilities";
		protected static final String ID = "ID";
		protected static final String ACTIVE = "Active";
		protected static final String TICKS = "Ticks";
		protected static final String INSTANCE_TAG = "InstanceData";
		/*private static final String MODIFIERS = "Modifiers";
		private static final String UUID_MOST = "IDMost";
		private static final String UUID_LEAST = "IDLeast";*/
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
				//Save Abilities
				NBTTagList abilities = new NBTTagList();
				for(AbilityInstance inst : instance.getAbilities())
				{
					NBTTagCompound anbt = new NBTTagCompound();
					anbt.setString(Keys.ID, inst.getAbility().getRegistryName().toString());
					anbt.setBoolean(Keys.ACTIVE, inst.isActive());
					anbt.setInteger(Keys.TICKS, inst.getTicksSinceStateChange());
					if(inst instanceof INBTSerializable)
					{
						anbt.setTag(Keys.INSTANCE_TAG, ((INBTSerializable<?>)inst).serializeNBT());
					}
					abilities.appendTag(anbt);
				}
				if(!abilities.hasNoTags())
				{
					nbt.setTag(Keys.ABILITIES, abilities);
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
				//Load abilities
				if(nbt.hasKey(Keys.ABILITIES, NBT.TAG_LIST))
				{
					NBTTagList abilities = nbt.getTagList(Keys.ABILITIES, NBT.TAG_COMPOUND);
					for(int i = 0; i < abilities.tagCount(); i++)
					{
						NBTTagCompound anbt = abilities.getCompoundTagAt(i);
						ResourceLocation id = new ResourceLocation(anbt.getString(Keys.ID));
						Ability a = Ability.REGISTRY.getObject(id);
						if(a != null)
						{
							AbilityInstance inst = a.createNewAbilityInstance(instance);
							if(inst != null)
							{
								if(anbt.hasKey(Keys.INSTANCE_TAG))
								{
									if(inst instanceof INBTSerializable<?>)
									{
										loadInstanceNBT(inst, anbt.getTag(Keys.INSTANCE_TAG));
									}
									else
									{
										FMLLog.warning("Character has saved data for ability with id %s, but that ability does not support additional data.", id);
									}
								}
								inst.load(anbt.getBoolean(Keys.ACTIVE), anbt.getInteger(Keys.TICKS));
							}
						}
						FMLLog.warning("Character has data saved for ability %s, but no such ability exists.", id);
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

			@SuppressWarnings("unchecked")
			public <T extends NBTBase> void loadInstanceNBT(AbilityInstance inst, T tag)
			{
				((INBTSerializable<T>)inst).deserializeNBT(tag);
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
