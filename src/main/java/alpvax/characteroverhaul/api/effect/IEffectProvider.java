package alpvax.characteroverhaul.api.effect;

import java.util.List;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.util.text.ITextComponent;

public interface IEffectProvider
{
	public ITextComponent getDisplayName();

	public default String getDisplayString()
	{
		return getDisplayName().getUnformattedText();
	}

	/**
	 * Use this to create new {@linkplain Effect} instances.
	 * @param affected the character the effects will be added to.
	 * @return a list of effects that will be added to the affected.
	 */
	public List<Effect> createEffects(ICharacter character);

	//public <T extends ICapabilityProvider> T getAttachedObject();
}
