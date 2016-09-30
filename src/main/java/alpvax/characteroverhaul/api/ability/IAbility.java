package alpvax.characteroverhaul.api.ability;

import java.util.UUID;

import alpvax.characteroverhaul.api.effect.IEffectProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAbility
{
	public UUID getId();

	public IEffectProvider getProvider();

	public String getLocalisedName();

	@SideOnly(Side.CLIENT)
	public void renderIcon();//TODO:Call it from somewhere

	/**
	 * Called when the ability is added to the character.
	 */
	public void onAttach();

	/**
	 * Called when the ability is removed from the character.
	 */
	public void onRemove();

	/**
	 * Called every tick, use it to passively enable/disable the ability.
	 */
	public boolean shouldToggle();

	/**
	 * Whether the ability is currently affecting the character.
	 */
	public boolean isActive();

	/**
	 * Called to start the ability. Use it to add ICharacterEffects
	 */
	public void trigger();

	/**
	 * Called to stop the ability. Use it to remove ICharacterEffects.
	 */
	public void reset();
}
