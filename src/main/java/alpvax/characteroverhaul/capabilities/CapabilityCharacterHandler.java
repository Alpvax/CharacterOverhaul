package alpvax.characteroverhaul.capabilities;

import java.util.concurrent.Callable;

import alpvax.characteroverhaul.api.character.AffectedBase;
import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.IAffected;
import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityCharacterHandler
{
	/*private static final class NBTKeys
	{
		private static final String PERKS = "Perks";
		private static final String SKILLS = "Skills";
		private static final String MODIFIERS = "Modifiers";
		//private static final String ABILITIES = "Abilities";
		//private static final String EFFECTS = "Effects";
		//protected static final String ABILITY_ACTIVE = "Active";
		//private static final String UUID_MOST = "IDMost";
		//private static final String UUID_LEAST = "IDLeast";
	}*/

	public static void register()
	{
		CapabilityManager.INSTANCE.register(ICharacter.class, new Capability.IStorage<ICharacter>()
		{
			@Override
			public NBTBase writeNBT(Capability<ICharacter> capability, ICharacter instance, EnumFacing side)
			{
				/*//Save Effects
				NBTTagCompound nbt = (NBTTagCompound)IAffected.CAPABILITY.writeNBT(instance, side);
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
					nbt.setTag(NBTKeys.PERKS, perks);
				}
				//Save Skills
				NBTTagCompound skills = new NBTTagCompound();
				for(Skill skill : Skill.REGISTRY.getValues())
				{
					SkillInstance inst = instance.getSkillInstance(skill);
					skills.setTag(skill.getRegistryName().toString(), inst.serializeNBT());
				}
				if(!skills.hasNoTags())
				{
					nbt.setTag(NBTKeys.SKILLS, skills);
				}*/
				/*//Save Modifiers
				NBTTagCompound modifiers = new NBTTagCompound();
				for(ResourceLocation key : CharacterModifierFactory.REGISTRY.getKeys())
				{
					ICharacterModifierHandler<?> modifier = instance.getModifierHandler(key);
					if(modifier != null)
					{
						modifiers.setTag(key.toString(), modifier.serializeNBT());
					}
				}
				if(!modifiers.hasNoTags())
				{
					nbt.setTag(NBTKeys.MODIFIERS, modifiers);
				}*/
				/*//Save Abilities
				NBTTagList abilities = new NBTTagList();
				for(IAbility ability : instance.getAbilities())
				{
					@SuppressWarnings("unchecked")
					NBTTagCompound tag = ability instanceof INBTSerializable ? ((INBTSerializable<NBTTagCompound>)ability).serializeNBT() : new NBTTagCompound();
					UUID id = ability.getId();
					tag.setLong(NBTKeys.UUID_MOST, id.getMostSignificantBits());
					tag.setLong(NBTKeys.UUID_LEAST, id.getLeastSignificantBits());
					tag.setBoolean(NBTKeys.ABILITY_ACTIVE, ability.isActive());
					abilities.appendTag(tag);
				}
				if(!abilities.hasNoTags())
				{
					nbt.setTag(NBTKeys.ABILITIES, abilities);
				}*/
				//return nbt;
				return null;
			}

			@Override
			public void readNBT(Capability<ICharacter> capability, ICharacter instance, EnumFacing side, NBTBase base)
			{
				/*NBTTagCompound nbt = (NBTTagCompound)base;
				//Load Effects
				IAffected.CAPABILITY.readNBT(instance, side, nbt);
				//Load Perks
				if(nbt.hasKey(NBTKeys.PERKS, NBT.TAG_COMPOUND))
				{
					NBTTagCompound perks = nbt.getCompoundTag(NBTKeys.PERKS);
					for(String s : perks.getKeySet())
					{
						Perk perk = Perk.REGISTRY.getValue(new ResourceLocation(s));
						if(perk != null)//Incorrect NBT check
						{
							instance.setPerkLevel(perk, perks.getInteger(s));
						}
					}
				}
				//Load Skills
				if(nbt.hasKey(NBTKeys.SKILLS, NBT.TAG_COMPOUND))
				{
					NBTTagCompound skills = nbt.getCompoundTag(NBTKeys.SKILLS);
					for(String s : skills.getKeySet())
					{
						Skill skill = Skill.REGISTRY.getValue(new ResourceLocation(s));
						if(skill != null)//Incorrect NBT check
						{
							SkillInstance inst = instance.getSkillInstance(skill);
							if(inst != null)
							{
								inst.deserializeNBT(skills.getCompoundTag(s));
							}
						}
					}
				}*/
				/*//Load Modifiers
				if(nbt.hasKey(NBTKeys.MODIFIERS, NBT.TAG_COMPOUND))
				{
					NBTTagCompound modifiers = nbt.getCompoundTag(NBTKeys.MODIFIERS);
					for(String s : modifiers.getKeySet())
					{
						ICharacterModifierHandler<?> modifier = instance.getModifierHandler(new ResourceLocation(s));
						if(modifier != null)
						{
							modifier.deserializeNBT(modifiers.getCompoundTag(s));
						}
					}
				}*/
				/*//Load abilities
				if(nbt.hasKey(NBTKeys.ABILITIES, NBT.TAG_LIST))
				{
					NBTTagList abilities = nbt.getTagList(NBTKeys.ABILITIES, NBT.TAG_COMPOUND);
					for(int i = 0; i < abilities.tagCount(); i++)
					{
						NBTTagCompound anbt = abilities.getCompoundTagAt(i);
						ResourceLocation id = new ResourceLocation(anbt.getString(NBTKeys.ID));
						Ability a = Ability.REGISTRY.getObject(id);
						if(a != null)
						{
							AbilityInstance inst = a.createNewAbilityInstance(instance);
							if(inst != null)
							{
								inst.deserializeNBT(anbt);
							}
						}
						FMLLog.warning("Character has data saved for ability %s, but no such ability exists.", id);
					}
				}*/
			}
		}, new Callable<ICharacter>()
		{
			@Override
			public ICharacter call() throws Exception
			{
				return new CharacterBase(new AffectedBase(null));
			}
		});
		CapabilityManager.INSTANCE.register(IAffected.class, new Capability.IStorage<IAffected>()
		{
			@Override
			public NBTBase writeNBT(Capability<IAffected> capability, IAffected instance, EnumFacing side)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				/*TODO://Save Effects
				NBTTagList effects = new NBTTagList();
				for(ICharacterEffect effect : instance.getEffects())
				{
					@SuppressWarnings("unchecked")
					NBTTagCompound tag = effect instanceof INBTSerializable ? ((INBTSerializable<NBTTagCompound>)effect).serializeNBT() : new NBTTagCompound();
					UUID id = effect.getId();
					tag.setLong(NBTKeys.UUID_MOST, id.getMostSignificantBits());
					tag.setLong(NBTKeys.UUID_LEAST, id.getLeastSignificantBits());
					effects.appendTag(tag);
				}
				if(!effects.hasNoTags())
				{
					nbt.setTag(NBTKeys.EFFECTS, effects);
				}*/
				return nbt;
			}

			@Override
			public void readNBT(Capability<IAffected> capability, IAffected instance, EnumFacing side, NBTBase base)
			{
				/*TODO:Load Effects
				NBTTagCompound nbt = (NBTTagCompound)base;*/
			}
		}, new Callable<IAffected>()
		{
			@Override
			public IAffected call() throws Exception
			{
				return new AffectedBase(null);
			}
		});
	}
}
