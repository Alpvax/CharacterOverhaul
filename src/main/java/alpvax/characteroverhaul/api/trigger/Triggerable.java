package alpvax.characteroverhaul.api.trigger;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Triggerable<T> implements INBTSerializable<NBTTagCompound>
{
	private boolean triggered;
	private Map<String, Trigger> triggers = Maps.newHashMap();
	private Map<String, Trigger> view = Collections.unmodifiableMap(triggers);

	protected abstract void trigger();

	protected abstract void reset();
	
	void setTriggered(boolean flag)
	{
		triggered = flag;
	}

	public boolean isTriggered()
	{
		return triggered;
	}

	@SuppressWarnings("unchecked")
	public T addTrigger(String key, Trigger trigger)
	{
		triggers.put(key, trigger);
		trigger.setTriggerable(this);
		return (T)this;
	}

	public Trigger getTrigger(String key)
	{
		return triggers.get(key);
	}

	public Trigger removeTrigger(String key)
	{
		Trigger trigger = triggers.remove(key);
		trigger.unregister();
		return trigger;
	}

	/**
	 * Returns an unmodifiable Map of all the triggers associated with this object, in <key, Trigger> pairs.
	 */
	public Map<String, Trigger> getTriggers()
	{
		return view;
	}

	/**
	 * Returns an unmodifiable Map of all the triggers associated with this object that are instances of the passed in
	 * class, in <key, Trigger> pairs.<br>
	 * To retrieve all triggers, use the no-parameters version: {@linkplain Triggerable#getTriggers()}.
	 * @param <U> Must either extend {@link Trigger}, or be an interface, such as {@link ITickable} or
	 *            {@link INBTSerializable}.
	 * @param classFilter the class to check for instances of.
	 */
	@SuppressWarnings("unchecked")
	public <U> Map<String, U> getTriggers(@Nonnull Class<U> classFilter)
	{
		if(classFilter.isAssignableFrom(Trigger.class))//If filter is Trigger or super, short-circuit
		{
			return (Map<String, U>)getTriggers();
		}
		return Collections.unmodifiableMap(triggers.entrySet().stream().filter(e -> classFilter.isAssignableFrom(e.getValue().getClass())).collect(Collectors.toMap(e -> e.getKey(), e -> classFilter.cast(e.getValue()))));
	}

	public void tickTriggers()
	{
		for(ITickable t : getTriggers(ITickable.class).values())
		{
			t.update();
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("Active", triggered);
		NBTTagCompound tag = new NBTTagCompound();
		for(Map.Entry<String, Trigger> e : triggers.entrySet())
		{
			Trigger t = e.getValue();
			if(t instanceof INBTSerializable)
			{
				tag.setTag(e.getKey(), ((INBTSerializable<?>)t).serializeNBT());
			}
		}
		if(!tag.hasNoTags())
		{
			nbt.setTag("Triggers", tag);
		}
		return nbt;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		triggered = nbt.getBoolean("Active");
		if(nbt.hasKey("Triggers", NBT.TAG_COMPOUND))
		{
			NBTTagCompound tag = nbt.getCompoundTag("Triggers");
			for(String key : tag.getKeySet())
			{
				Trigger t = getTrigger(key);
				if(t != null && t instanceof INBTSerializable)
				{
					((INBTSerializable)t).deserializeNBT(tag.getTag(key));
				}
			}
		}
	}


	private UUID id = UUID.randomUUID();

	public UUID getId()
	{
		return id;
	}
}
