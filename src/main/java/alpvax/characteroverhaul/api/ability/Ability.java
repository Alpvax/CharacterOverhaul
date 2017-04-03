package alpvax.characteroverhaul.api.ability;

import java.util.UUID;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.effect.IEffectProvider;
import alpvax.characteroverhaul.api.event.AbilityEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Ability implements INBTSerializable<NBTTagCompound>, ITickable, IEffectProvider
{
	//private final IAbilityProvider provider;
	private final ICharacter character;
	private UUID id;
	private boolean isEquipped = false;

	private boolean active = false;
	private int cooldown = 0;

	public Ability(/*IAbilityProvider provider, */ICharacter character)
	{
		//this.provider = provider;
		this.character = character;
		id = UUID.randomUUID();
	}

	/*protected final IAbilityProvider getProvider()
	{
		return provider;
	}*/

	protected ICharacter getCharacter()
	{
		return character;
	}

	public UUID getID()
	{
		return id;
	}

	public abstract ITextComponent getDisplayName();

	@SideOnly(Side.CLIENT)
	public abstract ResourceLocation getIconTexture();

	public int getCooldown()
	{
		return cooldown;
	}

	/**
	 * @return the cooldown of the ability in game ticks (~20 per second)
	 */
	public abstract int getMaxCooldown();

	/**
	 * Called when the ability is added to the character's ability hotbar.
	 */
	public void onEquip()
	{
		isEquipped = true;
		if(hasPassiveTrigger())
		{
			getCharacter().addEffect(getPassiveTrigger());
		}
	}

	/**
	 * Called when the ability is removed from the character's ability hotbar.
	 */
	public void onUnequip()
	{
		isEquipped = false;
		if(getCharacter().getEffect(getID()) != null)
		{
			getCharacter().removeEffect(getID());
		}
	}

	/**
	 * Whether the ability is currently affecting the character.
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * Default implementation checks that the ability is equipped to the hotbar and is off cooldown.
	 * @param isManual Whether the attempt was manual or passive.
	 * @return whether or not the ability can trigger
	 */
	protected boolean canTrigger(boolean isManual)
	{
		return isEquipped && cooldown <= 0;
	}

	/**
	 * Called to start the ability. Use it to add ICharacterEffects
	 */
	protected abstract void trigger();

	/**
	 * Called to stop the ability. Must reverse any effects of {@link #trigger()}.
	 */
	protected abstract void reset();

	/**
	 * Use this to set the cooldown of the ability.<br>
	 * Automatically called when ability is triggered.
	 * @param cd the number of ticks remaining for the cooldown. Negative numbers set the cooldown to the value of
	 *            {@link #getMaxCooldown()}.<br>
	 *            can be used to increase the cooldown above the maximum
	 */
	public final void setCooldown(int cd)
	{
		if(cd < 0)
		{
			cooldown = getMaxCooldown();
		}
		else
		{
			cooldown = cd;
		}
	}

	/**
	 * Whether or not the ability can be manually triggered.<br>
	 * Is used to determine whether to display the icon as activateable or passive,<br>
	 * so should not take into account cooldown etc.
	 */
	public boolean hasManualTrigger()
	{
		return true;
	}

	/**
	 * Call this to attempt to trigger the ability
	 * @return
	 */
	public boolean attemptTrigger()
	{
		if(getCharacter().getWorld().isRemote)
		{
			//TODO:CharacterNetwork.sendToServer(new AbilityTriggerPacket(abilitySlot));
			//return false;
		}
		else if(canTrigger(true))
		{
			AbilityEvent.Trigger event = new AbilityEvent.Trigger(getCharacter(), this);
			if(MinecraftForge.EVENT_BUS.post(event))
			{
				return false;
			}
			trigger();
			cooldown = event.getCooldown();
			return true;
		}
		return false;
	}

	/**
	 * Used to update cooldown client side.<br>
	 * Overide to run any other client-side code.
	 * @param cooldown
	 */
	@SideOnly(Side.CLIENT)
	public void clientTrigger(int cooldown)
	{
		this.cooldown = cooldown;
	}

	public boolean hasPassiveTrigger()
	{
		return false;
	}

	protected EffectAbilityTrigger getPassiveTrigger()
	{
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("Active", active);
		nbt.setUniqueId("ID", getID());
		nbt.setInteger("Cooldown", cooldown);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		active = nbt.getBoolean("Active");
		id = nbt.getUniqueId("ID");
		cooldown = nbt.getInteger("Cooldown");
	}

	/**
	 * Used to sync data to the client.
	 * @return the data to be sent to the client
	 */
	public NBTTagCompound getNBTForClientSync()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("Active", active);
		nbt.setUniqueId("ID", getID());
		nbt.setInteger("Cooldown", cooldown);
		return nbt;
	}


	/**
	 * Used to read all synchronised data on the client.
	 */
	public void readClientData(NBTTagCompound nbt)
	{
		active = nbt.getBoolean("Active");
		id = nbt.getUniqueId("ID");
		cooldown = nbt.getInteger("Cooldown");
	}

	@Override
	public void update()
	{
		if(cooldown > 0)
		{
			cooldown--;
		}
	}
}
