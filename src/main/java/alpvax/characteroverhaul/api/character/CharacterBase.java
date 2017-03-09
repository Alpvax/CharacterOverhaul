package alpvax.characteroverhaul.api.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.config.CharacterConfig;
import alpvax.characteroverhaul.api.effect.Effect;
import alpvax.characteroverhaul.api.event.AbilityEvent;
import alpvax.characteroverhaul.api.event.CharacterEvent;
import alpvax.characteroverhaul.api.event.EffectEvent;
import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.api.skill.Skill;
import alpvax.characteroverhaul.api.skill.SkillInstance;
import alpvax.characteroverhaul.api.trigger.Trigger.TriggerAttach;
import alpvax.characteroverhaul.api.trigger.Triggerable;
import alpvax.characteroverhaul.network.AbilityChangedPacket;
import alpvax.characteroverhaul.network.AbilityEquippedPacket;
import alpvax.characteroverhaul.network.AbstractPacket;
import alpvax.characteroverhaul.network.CharacterNetwork;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Basic implementation of the {@link ICharacter} interface.
 */
@EventBusSubscriber
public /*abstract/**/ class CharacterBase implements ICharacter
{
	private final Map<ResourceLocation, Integer> perks = new HashMap<>();
	private final ImmutableMap<ResourceLocation, SkillInstance> skills;
	//private final Map<ResourceLocation, ICharacterModifier> modifiers;
	private Map<UUID, Effect> effects = new HashMap<>();
	private Map<UUID, Ability> abilities = new HashMap<>();
	private UUID[] abilityHotbar = new UUID[CharacterConfig.numAbilities];

	private final ICapabilityProvider attached;

	public CharacterBase(ICapabilityProvider object)
	{
		attached = object;
		CharacterEvent.CharacterCreate event = new CharacterEvent.CharacterCreate(this);
		MinecraftForge.EVENT_BUS.post(event);
		skills = ImmutableMap.<ResourceLocation, SkillInstance> builder().putAll(event.getSkills().stream().collect(Collectors.toMap(Skill::getRegistryName, s -> new SkillInstance(this, s)))).build();
		//modifiers = Maps.newHashMap(event.getModifiers());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ICapabilityProvider> T getAttachedObject()
	{
		return (T)attached;
	}

	@Override
	public World getWorld()
	{
		ICapabilityProvider o = getAttachedObject();
		if(o instanceof Entity)
		{
			return ((Entity)o).getEntityWorld();
		}
		if(o instanceof TileEntity)
		{
			return ((TileEntity)o).getWorld();
		}
		return null;
	}

	@Override
	public Vec3d getPosition()
	{
		ICapabilityProvider o = getAttachedObject();
		if(o instanceof Entity)
		{
			return ((Entity)o).getPositionVector();
		}
		if(o instanceof TileEntity)
		{
			BlockPos pos = ((TileEntity)o).getPos();
			return new Vec3d((pos.getX()) + 0.5D, (pos.getY()) + 0.5D, (pos.getZ()) + 0.5D);
		}
		return null;
	}

	@Override
	public Vec3d getDirection()
	{
		ICapabilityProvider o = getAttachedObject();
		if(o instanceof Entity)
		{
			return ((Entity)o).getLookVec();
		}
		if(o instanceof TileEntity)
		{
			TileEntity t = ((TileEntity)o);
			IBlockState state = t.getWorld().getBlockState(t.getPos());
			if(state.getPropertyKeys().contains(BlockDirectional.FACING))
			{
				return new Vec3d(state.getValue(BlockDirectional.FACING).getDirectionVec());
			}
		}
		return Vec3d.ZERO;
	}

	@Override
	public int getPerkLevel(Perk perk)
	{
		Integer i = perks.get(perk.getRegistryName());
		return i != null ? i.intValue() : 0;
	}

	@Override
	public void aquirePerk(Perk perk)
	{
		setPerkLevel(perk, getPerkLevel(perk) + 1);
	}

	@Override
	public void aquirePerk(Perk perk, int level)
	{
		if(level > getPerkLevel(perk))
		{
			setPerkLevel(perk, level);
		}
	}

	private void setPerkLevel(Perk perk, int level)
	{
		int oldLevel = getPerkLevel(perk);
		CharacterEvent.AquirePerk event = new CharacterEvent.AquirePerk(this, perk, oldLevel, level);
		if(!MinecraftForge.EVENT_BUS.post(event) && (level = event.getNewLevel()) != oldLevel)
		{
			perks.put(perk.getRegistryName(), Integer.valueOf(level));
			perk.onLevelChange(oldLevel, level, this);
		}
	}

	@Override
	public SkillInstance getSkillInstance(@Nonnull Skill skill)
	{
		return skills.get(skill.getRegistryName());
	}

	@Override
	public int getSkillLevel(Skill skill)
	{
		SkillInstance inst = getSkillInstance(skill);
		if(inst != null)
		{
			return inst.getLevel();
		}
		//TODO:Log
		return Integer.MIN_VALUE;
	}

	@Override
	public void addSkillExperience(Skill skill, float amount)
	{
		SkillInstance inst = getSkillInstance(skill);
		if(inst != null)
		{
			inst.addExp(amount);
		}
	}

	@Override
	public List<Effect> getEffects()
	{
		return new ArrayList<>(effects.values());
	}

	/*@Override
	public void addEffects(IEffectProvider provider)
	{
		CharacterEvent.AddEffect event = new CharacterEvent.AddEffect(this, provider);
		if(MinecraftForge.EVENT_BUS.post(event))
		{
			return;
		}
		//TODO:Add provider
		for(Effect effect : event.getEffects())
		{
			addEffect(effect);
		}
	}*/

	@Override
	public void addEffect(Effect effect)
	{
		if(!MinecraftForge.EVENT_BUS.post(new EffectEvent.Add(this, effect)))
		{
			UUID id = effect.getId();
			Preconditions.checkArgument(!effects.containsKey(id), "Already an effect with that id: %s", id);
			notifyAttachTriggers(effect, true);
			effects.put(id, effect);
		}
	}

	@Override
	public Effect getEffect(UUID effectID)
	{
		return effects.get(effectID);
	}

	@Override
	public void removeEffect(UUID id)
	{
		if(effects.containsKey(id))
		{
			Effect effect = effects.remove(id);
			if(effect != null && !MinecraftForge.EVENT_BUS.post(new EffectEvent.Remove(this, effect)))
			{
				notifyAttachTriggers(effect, false);
			}
		}
	}

	@Override
	public Ability[] getHotbarAbilities()
	{
		return Arrays.stream(abilityHotbar).map(u -> abilities.get(u)).toArray(Ability[]::new);
	}

	@Override
	public List<Ability> getAllAbilities()
	{
		return Collections.unmodifiableList(new ArrayList<>(abilities.values()));
	}

	/*@Override
	public void addAbilities(IAbilityProvider provider)
	{
		CharacterEvent.AddAbility event = new CharacterEvent.AddAbility(this, provider);
		if(MinecraftForge.EVENT_BUS.post(event))
		{
			return;
		}
		//TODO:Add provider
		for(Ability ability : event.getAbilities())
		{
			addAbility(ability);
		}
	}*/

	@Override
	public void triggerAbility(int slot)
	{
		if(slot >= 0 && slot < abilityHotbar.length)
		{
			Ability ability = getHotbarAbilities()[slot];
			if(ability.hasManualTrigger())
			{
				ability.attemptTrigger();
			}
		}
	}

	public void addAbility(Ability ability)
	{
		if(!MinecraftForge.EVENT_BUS.post(new AbilityEvent.Add(this, ability)))
		{
			UUID id = ability.getID();
			Preconditions.checkArgument(!abilities.containsKey(id), "Already an ability with that id: %s", id);
			ability.onEquip();
			abilities.put(id, ability);
			sendPacketToClient(new AbilityChangedPacket(id, ability));
		}
	}

	@Override
	public void removeAbility(UUID id)
	{
		if(abilities.containsKey(id))
		{
			Ability ability = abilities.remove(id);
			if(ability != null && !MinecraftForge.EVENT_BUS.post(new AbilityEvent.Remove(this, ability)))
			{
				ability.onUnequip();
			}
		}
	}

	@Override
	public void setHotbarSlot(int i, UUID id)
	{
		abilityHotbar[i] = id;
		sendPacketToClient(new AbilityEquippedPacket(id, i));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateClientAbility(UUID id, Ability ability)
	{
		if(ability == null)
		{
			abilities.remove(id);
		}
		else
		{
			abilities.put(id, ability);
		}
	}

	private void notifyAttachTriggers(Triggerable<?> t, boolean attach)
	{
		if(t == null)
		{
			return;
		}
		for(TriggerAttach trigger : t.getTriggers(TriggerAttach.class).values())
		{
			if(attach)
			{
				trigger.onAttach();
			}
			else
			{
				trigger.onDetach();
			}
		}
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public <T extends ICharacterModifierHandler<?>> T getModifierHandler(ResourceLocation registryName)
	{
		return (T)modifiers.get(registryName);
	}*/

	@Override
	public void cloneFrom(ICharacter oldCharacter)
	{
		for(Perk perk : Perk.getAllPerks())
		{
			int l = oldCharacter.getPerkLevel(perk);
			if(l != 0)
			{
				setPerkLevel(perk, l);
			}
		}
		for(Skill skill : Skill.getAllSkills())
		{
			SkillInstance inst = oldCharacter.getSkillInstance(skill);
			if(inst != null)
			{
				inst.cloneTo(getSkillInstance(skill));
			}
		}
		//TODO:Complete cloning
	}

	private static final class NBTKeys
	{
		private static final String PERKS = "Perks";
		private static final String SKILLS = "Skills";
		private static final String EFFECTS = "Effects";
		private static final String ABILITIES = "Abilities";
		private static final String HOTBAR = "Hotbar";
		private static final String UUID = "ID";
		private static final String SLOT = "Slot";
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		//Save Perks
		NBTTagCompound perknbt = new NBTTagCompound();
		for(Perk perk : Perk.getAllPerks())
		{
			int l = getPerkLevel(perk);
			if(l != 0)
			{
				perknbt.setInteger(perk.getRegistryName().toString(), l);
			}
		}
		if(!perknbt.hasNoTags())
		{
			nbt.setTag(NBTKeys.PERKS, perknbt);
		}
		//Save Skills
		NBTTagCompound skillnbt = new NBTTagCompound();
		for(SkillInstance inst : skills.values())
		{
			skillnbt.setTag(inst.getSkill().getRegistryName().toString(), inst.serializeNBT());
		}
		if(!skillnbt.hasNoTags())
		{
			nbt.setTag(NBTKeys.SKILLS, skillnbt);
		}
		//Save Abilities
		NBTTagList abilitynbt = new NBTTagList();
		for(Ability ability : abilities.values())
		{
			NBTTagCompound tag = ability.serializeNBT();
			/*UUID id = ability.getID();
			tag.setUniqueId(NBTKeys.UUID, id);*/
			abilitynbt.appendTag(tag);
		}
		if(!abilitynbt.hasNoTags())
		{
			nbt.setTag(NBTKeys.ABILITIES, abilitynbt);
		}
		//Save Hotbar
		NBTTagList hotbarnbt = new NBTTagList();
		for(int i = 0; i < abilityHotbar.length; i++)
		{
			NBTTagCompound tag = new NBTTagCompound();
			UUID id = abilityHotbar[i];
			if(id != null)
			{
				tag.setUniqueId(NBTKeys.UUID, id);
				tag.setInteger(NBTKeys.SLOT, i);
			}
			hotbarnbt.appendTag(tag);
		}
		if(!hotbarnbt.hasNoTags())
		{
			nbt.setTag(NBTKeys.HOTBAR, abilitynbt);
		}
		//Save Effects
		NBTTagList effectnbt = new NBTTagList();
		for(Effect effect : effects.values())
		{
			NBTTagCompound tag = effect.serializeNBT();
			/*UUID id = effect.getId();
			tag.setUniqueId(NBTKeys.UUID, id);*/
			effectnbt.appendTag(tag);
		}
		if(!effectnbt.hasNoTags())
		{
			nbt.setTag(NBTKeys.EFFECTS, effectnbt);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		//Load Perks
		if(nbt.hasKey(NBTKeys.PERKS, NBT.TAG_COMPOUND))
		{
			NBTTagCompound perknbt = nbt.getCompoundTag(NBTKeys.PERKS);
			for(String s : perknbt.getKeySet())
			{
				Perk perk = Perk.REGISTRY.getValue(new ResourceLocation(s));
				if(perk != null)//Incorrect NBT check
				{
					setPerkLevel(perk, perknbt.getInteger(s));
				}
			}
		}
		//Load Skills
		if(nbt.hasKey(NBTKeys.SKILLS, NBT.TAG_COMPOUND))
		{
			NBTTagCompound skillnbt = nbt.getCompoundTag(NBTKeys.SKILLS);
			for(String s : skillnbt.getKeySet())
			{
				Skill skill = Skill.REGISTRY.getValue(new ResourceLocation(s));
				if(skill != null)//Incorrect NBT check
				{
					SkillInstance inst = getSkillInstance(skill);
					if(inst != null)
					{
						inst.deserializeNBT(skillnbt.getCompoundTag(s));
					}
				}
			}
		}
		//Load Abilities
		if(nbt.hasKey(NBTKeys.ABILITIES, NBT.TAG_LIST))
		{
			NBTTagList abilitynbt = nbt.getTagList(NBTKeys.ABILITIES, NBT.TAG_LIST);
			for(int i = 0; i < abilitynbt.tagCount(); i++)
			{
				NBTTagCompound tag = abilitynbt.getCompoundTagAt(i);
				//TODO:Load Ability
			}
		}
		//Load Hotbar
		if(nbt.hasKey(NBTKeys.HOTBAR, NBT.TAG_LIST))
		{
			NBTTagList hotbarnbt = nbt.getTagList(NBTKeys.HOTBAR, NBT.TAG_LIST);
			for(int i = 0; i < hotbarnbt.tagCount(); i++)
			{
				NBTTagCompound tag = hotbarnbt.getCompoundTagAt(i);
				setHotbarSlot(tag.getInteger(NBTKeys.SLOT), tag.getUniqueId(NBTKeys.UUID));
			}
		}
		//Load Effects
		if(nbt.hasKey(NBTKeys.EFFECTS, NBT.TAG_LIST))
		{
			NBTTagList effectnbt = nbt.getTagList(NBTKeys.EFFECTS, NBT.TAG_LIST);
			for(int i = 0; i < effectnbt.tagCount(); i++)
			{
				NBTTagCompound tag = effectnbt.getCompoundTagAt(i);
				//TODO:Load Effect
			}
		}
	}

	private boolean finishedLoading = false;

	private void sendPacketToClient(AbstractPacket packet)
	{
		if(finishedLoading && getAttachedObject() instanceof EntityPlayerMP)
		{
			CharacterNetwork.sendTo(packet, getAttachedObject());
		}
	}

	private void syncAll()
	{
		for(Entry<UUID, Ability> e : abilities.entrySet())
		{
			sendPacketToClient(new AbilityChangedPacket(e.getKey(), e.getValue()));
		}
		for(int i = 0; i < abilityHotbar.length; i++)
		{
			UUID id = abilityHotbar[i];
			if(id != null && abilities.get(id) != null)
			{
				sendPacketToClient(new AbilityEquippedPacket(id, i));
			}
		}
	}

	// *********************** Event Handlers **************************************

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void syncInitialData(PlayerLoggedInEvent event)
	{
		ICharacter character = event.player.getCapability(ICharacter.CAPABILITY, null);
		if(character instanceof CharacterBase)
		{
			CharacterBase c = (CharacterBase)character;
			c.finishedLoading = true;
			c.syncAll();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onRespawnPlayer(PlayerEvent.Clone event)
	{//TODO: improve cloning
		ICharacter oldCharacter = event.getOriginal().getCapability(ICharacter.CAPABILITY, null);
		if(!oldCharacter.getWorld().isRemote)
		{
			ICharacter newCharacter = event.getEntityPlayer().getCapability(ICharacter.CAPABILITY, null);
			newCharacter.cloneFrom(oldCharacter);
		}
	}
}