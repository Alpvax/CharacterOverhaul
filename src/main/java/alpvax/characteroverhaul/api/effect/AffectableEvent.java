package alpvax.characteroverhaul.api.effect;

import java.util.List;

import alpvax.characteroverhaul.api.affectable.Affectable;
import alpvax.characteroverhaul.api.affectable.IAffectable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AffectableEvent extends Event
{
	private final IAffectable affectable;

	public AffectableEvent(IAffectable affectable)
	{
		this.affectable = affectable;
	}

	public IAffectable getAffectable()
	{
		return affectable;
	}

	/**
	 * AddEffect is fired when an {@linkplain IEffectProvider} is added to an affectable.<br>
	 * <br>
	 * This event is fired from {@link Affectable#addEffects(IEffectProvider)}.<br>
	 * <br>
	 * {@link #effects} contains the ArrayList of Effects that will be added.<br>
	 * <br>
	 * This event is {@link Cancelable}.<br>
	 * If it is canceled, the provider is not added to the character.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	@Cancelable
	public static class AddEffect extends AffectableEvent
	{
		private final List<Effect> effects;

		public AddEffect(IAffectable affectable, List<Effect> effects)
		{
			super(affectable);
			this.effects = effects;
		}

		public List<Effect> getEffects()
		{
			return effects;
		}
	}
}
