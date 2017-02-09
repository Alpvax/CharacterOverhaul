package alpvax.characteroverhaul.api.ability;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.trigger.Trigger.TriggerKeybind;
import alpvax.characteroverhaul.api.trigger.Triggerable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Ability extends Triggerable<Ability>
{
	//private final IAbilityProvider provider;
	private final ICharacter character;

	public Ability(/*IAbilityProvider provider, */ICharacter character)
	{
		//this.provider = provider;
		this.character = character;
	}

	/*protected final IAbilityProvider getProvider()
	{
		return provider;
	}*/

	protected ICharacter getCharacter()
	{
		return character;
	}

	public abstract ITextComponent getDisplayName();

	@SideOnly(Side.CLIENT)
	protected abstract ResourceLocation getIconTexture();

	@SideOnly(Side.CLIENT)
	protected abstract boolean shouldRenderOnHUD();

	/**
	 * Called when the ability is added to the character's ability hotbar.
	 */
	public void onEquip()
	{}

	/**
	 * Called when the ability is removed from the character's ability hotbar.
	 */
	public void onUnequip()
	{}

	/**
	 * Whether the ability is currently affecting the character.
	 */
	public boolean isActive()
	{
		return isTriggered();
	}

	/**
	 * Called to start the ability. Use it to add ICharacterEffects
	 */
	@Override
	protected abstract void trigger();

	/**
	 * Called to stop the ability. Must reverse any effects of {@link #trigger()}.
	 */
	@Override
	protected abstract void reset();

	/**
	 * Performs the function passed in when the keybind is triggered.<br>
	 * Lambda use is recommended (Otherwise why not just subclass {@linkplain TriggerKeybind}?)<br>
	 * Registers trigger with the key "keybind", so use that if you wish to remove the trigger.
	 * @return this so the function can be chained.
	 */
	public Ability addKeybindTrigger(final Runnable keyPressHandler)
	{
		return addTrigger("keybind", new TriggerKeybind()
		{
			@Override
			public void onKeyPressed()
			{
				keyPressHandler.run();
			}
		});
	}

	public boolean hasKeybind()
	{
		return getTrigger("keybind") != null;
	}

	public TriggerKeybind getKeybindTrigger()
	{
		return (TriggerKeybind)getTrigger("keybind");
	}
}
