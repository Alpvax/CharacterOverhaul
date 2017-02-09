package alpvax.characteroverhaul.api.trigger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.Level;

import com.google.common.base.Preconditions;

import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Trigger
{
	private Triggerable<?> triggerable = null;
	private int disabled = 0;

	public void setTriggerable(Triggerable<?> triggerable)
	{
		Preconditions.checkArgument(this.triggerable == null, "Triggerable has already been set to %s.", this.triggerable);
		this.triggerable = triggerable;
	}

	protected void trigger()
	{
		if(disabled < 1)
		{
			triggerable.trigger();
			triggerable.setTriggered(true);
		}
	}

	protected void reset()
	{
		if(disabled < 1)
		{
			triggerable.reset();
			triggerable.setTriggered(false);
		}
	}

	protected boolean isTriggered()
	{
		return triggerable.isTriggered();
	}

	/**
	 * Use to implement silence/break effects
	 */
	public void disable()
	{
		disabled++;
	}

	/**
	 * Use to implement silence/break effects
	 */
	public void enable()
	{
		disabled--;
	}

	/**
	 * Use this to unregister anything that this registered (e.g. eventhandlers).
	 */
	void unregister()
	{}


	//TRIGGERS

	public static final TriggerAttach ALWAYS_ACTIVE = new TriggerAttach()
	{
		@Override
		public void onDetach()
		{
			reset();
		}

		@Override
		public void onAttach()
		{
			trigger();
		}
	};

	public static abstract class TriggerKeybind extends Trigger
	{
		public abstract void onKeyPressed();
	}

	/**
	 * Used to add a trigger with an event handler.<br>
	 * Automatically registers all methods annotated with {@link SubscribeEvent}, and unregisters them when the effect
	 * is removed.<br>
	 * With no @SubscribeEvent methods, this does nothing.
	 * @author Alpvax
	 *
	 * @param <T> the Event type
	 */
	public static abstract class TriggerEvent extends Trigger
	{
		private final EventBus eventbus;

		public TriggerEvent()
		{
			this(MinecraftForge.EVENT_BUS);
		}
		public TriggerEvent(EventBus bus)
		{
			Preconditions.checkArgument(checkClassForEvents(getClass()), "TriggerEvent %s has no methods annotated with @SubscribeEvent.", getClass());
			(eventbus = bus).register(this);
		}

		@Override
		void unregister()
		{
			eventbus.unregister(this);
		}

		private static boolean checkClassForEvents(Class<? extends TriggerEvent> cls)
		{
			boolean flag = false;
			for(Method m : cls.getDeclaredMethods())
			{
				int mods = m.getModifiers();
				if(Modifier.isStatic(mods) || !Modifier.isPublic(mods))
				{
					continue;
				}
				if(m.isAnnotationPresent(SubscribeEvent.class))
				{
					flag = true;
				}
				else
				{
					Class<?>[] params = m.getParameterTypes();
					if(params.length == 1 && Event.class.isAssignableFrom(params[0]))
					{
						FMLLog.log(Level.DEBUG, "Found method %s with single Event parameter. Should it have an @SubscribeEvent annotation?", m);
					}
				}
			}
			return flag;
		}
	}

	public static abstract class TriggerAttach extends Trigger
	{
		/**
		 * Called when the triggerable is added to something.
		 */
		public abstract void onAttach();

		/**
		 * Called when the triggerable is removed from whatever it was attached to.
		 */
		public abstract void onDetach();
	}

	/**
	 * Used to add a tick-based trigger. Called every tick for Abilities and Effects.
	 */
	public static abstract class TriggerTimed extends Trigger implements ITickable, INBTSerializable<NBTTagInt>
	{
		private int ticks = 0;

		@Override
		public void update()
		{
			ticks++;
			if(isTriggered())
			{
				if(shouldReset(ticks))
				{
					reset();
				}
			}
			else
			{
				if(shouldTrigger(ticks))
				{
					trigger();
				}
			}
		}

		protected abstract boolean shouldTrigger(int ticksSinceReset);

		protected abstract boolean shouldReset(int ticksSinceTriggered);

		@Override
		public NBTTagInt serializeNBT()
		{
			return new NBTTagInt(ticks);
		}

		@Override
		public void deserializeNBT(NBTTagInt nbt)
		{
			ticks = nbt.getInt();
		}
	}

	public static abstract class TriggerRanged extends Trigger implements ITickable
	{
		/*TODO:Trigger/reset based on range
		@Override(non-Javadoc)
		public void update()
		{
			
		}*/
	}
}
