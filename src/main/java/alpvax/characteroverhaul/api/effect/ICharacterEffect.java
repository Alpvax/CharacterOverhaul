package alpvax.characteroverhaul.api.effect;

import java.util.UUID;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICharacterEffect
{
	public UUID getId();

	public IEffectProvider getProvider();

	public String getLocalisedName();

	/**
	 * Called every tick, use it to affect the character or other objects around the character.
	 */
	public void tick();

	/**
	 * Called when the effect is added to the character.
	 */
	public void onAttach();

	/**
	 * Called when the effect is removed from the character.
	 */
	public void onRemove();

	@SideOnly(Side.CLIENT)
	public void renderOnHUD(ScaledResolution resolution);

	@SideOnly(Side.CLIENT)
	public void renderInGUI();//TODO: effect gui
}
