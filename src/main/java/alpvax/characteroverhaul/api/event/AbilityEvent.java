package alpvax.characteroverhaul.api.event;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.character.CharacterBase;
import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class AbilityEvent extends CharacterEvent
{
	private final Ability ability;

	public AbilityEvent(ICharacter character, Ability ability)
	{
		super(character);
		this.ability = ability;
	}

	public Ability getAbility()
	{
		return ability;
	}

	/**
	 * AbilityEvent.Add is fired when an {@linkplain Ability} is added to a character.<br>
	 * <br>
	 * This event is fired from {@link CharacterBase#addAbility(Ability)}.<br>
	 * <br>
	 * This event is {@link Cancelable}.<br>
	 * If it is canceled, the ability is not added to the character.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	@Cancelable
	public static class Add extends AbilityEvent
	{
		public Add(ICharacter character, Ability ability)
		{
			super(character, ability);
		}
	}

	/**
	 * AbilityEvent.Remove is fired when an {@linkplain Ability} is removed from a character.<br>
	 * <br>
	 * This event is fired from {@link CharacterBase#removeAbility(java.util.UUID)}.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	public static class Remove extends AbilityEvent
	{
		public Remove(ICharacter character, Ability ability)
		{
			super(character, ability);
		}
	}

	/**
	 * Trigger is fired when a character triggers an ability.<br>
	 * <br>
	 * This event is fired from method {@link Ability#attemptTrigger()}.<br>
	 * <br>
	 * {@link #cooldown is the cooldown to set the ability to} <br>
	 * This event is {@link Cancelable}.<br>
	 * If it is canceled the ability is not triggered.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
	 **/
	@Cancelable
	public static class Trigger extends AbilityEvent
	{
		private int cooldown;

		public Trigger(ICharacter character, Ability ability)
		{
			super(character, ability);
			cooldown = getAbility().getMaxCooldown();
		}

		public int getCooldown()
		{
			return cooldown;
		}

		public void setCooldown(int cooldown)
		{
			this.cooldown = cooldown;
		}
	}
}
