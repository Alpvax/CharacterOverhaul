package alpvax.characteroverhaul.api.affectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;

import alpvax.characteroverhaul.api.effect.Effect;
import alpvax.characteroverhaul.api.effect.IEffectProvider;
import alpvax.characteroverhaul.api.trigger.Trigger.TriggerAttach;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class Affectable implements IAffectable
{
	private ICapabilityProvider attached = null;
	private Map<UUID, Effect> effects = new HashMap<>();


	public Affectable setAttached(ICapabilityProvider attachTo)
	{
		Preconditions.checkNotNull(attached, "Affectable already attached to provider: %s", attached);
		attached = attachTo;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ICapabilityProvider> T getAttachedObject()
	{
		return (T)attached;
	}

	@Override
	public List<Effect> getEffects()
	{
		return new ArrayList<>(effects.values());
	}

	@Override
	public void addEffects(IEffectProvider provider)
	{
		/*AffectableEvent.AddEffect event = new AffectableEvent.AddEffect(this, provider);
		if(MinecraftForge.EVENT_BUS.post(event))
		{
			return;
		}
		//TODO:Add provider
		for(Effect effect : event.getEffects())
		{
			addEffect(effect);
		}*/
	}

	private void addEffect(Effect effect)
	{
		if(effect != null)
		{
			UUID id = effect.getId();
			Preconditions.checkArgument(!effects.containsKey(id), "Already an effect with that id: %s", id);
			effects.put(id, effect);
			for(TriggerAttach trigger : effect.getTriggers(TriggerAttach.class).values())
			{
				trigger.onAttach();
			}
		}
	}

	@Override
	public void removeEffect(UUID id)
	{
		if(effects.containsKey(id))
		{
			for(TriggerAttach trigger : effects.remove(id).getTriggers(TriggerAttach.class).values())
			{
				trigger.onDetach();
			}
		}
	}
}
