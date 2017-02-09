package alpvax.characteroverhaul.api.effect;

import alpvax.characteroverhaul.api.trigger.Triggerable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Effect extends Triggerable<Effect>
{
	private final IEffectProvider provider;

	public Effect(IEffectProvider provider)
	{
		this.provider = provider;
	}

	public final IEffectProvider getProvider()
	{
		return provider;
	}

	public abstract ITextComponent getDisplayName();

	@SideOnly(Side.CLIENT)
	public abstract ResourceLocation getIconTexture();

	@SideOnly(Side.CLIENT)
	public abstract boolean shouldRenderOnHUD();

	public abstract boolean isPositiveEffect();

	public abstract boolean canBePurged();

	/**
	 * Called while the effect is on the character.
	 */
	public abstract void tickEffect();

	/**
	 * Called to start the effect. Use it to add AttributeModifiers, or launch projectiles.
	 */
	@Override
	protected abstract void trigger();

	/**
	 * Called to stop the effect. Must reverse any effects of {@link #trigger()}.
	 */
	@Override
	protected abstract void reset();
}
