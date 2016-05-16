package alpvax.characteroverhaul.api.ability;

import java.util.List;
import java.util.UUID;

import alpvax.characteroverhaul.api.effect.ICharacterEffect;

public interface IAbility
{
	public class EffectEntry
	{
		public final ICharacterEffect effect;
		/** Active when the ability is active */
		public final boolean activeState;
		/** Active when the ability is inactive */
		public final boolean inactiveState;

		public EffectEntry(ICharacterEffect effect)
		{
			this(effect, true, false);
		}

		public EffectEntry(ICharacterEffect effect, boolean active, boolean inactive)
		{
			this.effect = effect;
			activeState = active;
			inactiveState = inactive;
		}
	}

	public static enum AbilityState
	{
		ACTIVE, INACTIVE, DISABLED;
	}

	public UUID getID();

	public default AbilityState getDefaultState()
	{
		return AbilityState.INACTIVE;
	}

	public List<EffectEntry> getAllEffects();
}
