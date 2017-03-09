package alpvax.characteroverhaul.api.event;

import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.effect.Effect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class EffectEvent extends CharacterEvent
{
	private final Effect effect;

	public EffectEvent(ICharacter character, Effect effect)
	{
		super(character);
		this.effect = effect;
	}

	public Effect getEffect()
	{
		return effect;
	}

	/**
	 * EffectEvent.Add is fired when an {@linkplain Effect} is added to a character.<br>
	 * <br>
	 * This event is fired from {@link CharacterBase#addEffect(Effect)}.<br>
	 * <br>
	 * This event is {@link Cancelable}.<br>
	 * If it is canceled, the effect is not added to the character.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	@Cancelable
	public static class Add extends EffectEvent
	{
		public Add(ICharacter character, Effect effect)
		{
			super(character, effect);
		}
	}

	/**
	 * EffectEvent.Remove is fired when an {@linkplain Effect} is removed from a character.<br>
	 * <br>
	 * This event is fired from {@link CharacterBase#removeEffect(java.util.UUID)}.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	public static class Remove extends EffectEvent
	{
		public Remove(ICharacter character, Effect effect)
		{
			super(character, effect);
		}
	}

	/**
	 * Trigger is fired when a character triggers an effect.<br>
	 * <br>
	 * This event is fired from method {@link Effect#attemptTrigger()}.<br>
	 * <br>
	 * {@link #cooldown is the cooldown to set the effect to} <br>
	 * This event is {@link Cancelable}.<br>
	 * If it is canceled the effect is not triggered.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	/*@Cancelable
	public static class Trigger extends EffectEvent
	{
		private int cooldown;
	
		public Trigger(ICharacter character, Effect effect)
		{
			super(character, effect);
			cooldown = getEffect().getMaxCooldown();
		}
	
		public int getCooldown()
		{
			return cooldown;
		}
	
		public void setCooldown(int cooldown)
		{
			this.cooldown = cooldown;
		}
	}*/
}

