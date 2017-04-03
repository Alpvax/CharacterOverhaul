package alpvax.characteroverhaul.api.affectable;

import java.util.List;
import java.util.UUID;

import alpvax.characteroverhaul.api.effect.Effect;
import alpvax.characteroverhaul.api.effect.IEffectProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IAffectable
{
	@CapabilityInject(IAffectable.class)
	public static Capability<IAffectable> CAPABILITY = null;

	/**
	 * Gets the object this represents.
	 * @return
	 */
	public <T extends ICapabilityProvider> T getAttachedObject();

	public List<Effect> getEffects();

	public void addEffects(IEffectProvider provider);

	public void removeEffect(UUID id);
}
