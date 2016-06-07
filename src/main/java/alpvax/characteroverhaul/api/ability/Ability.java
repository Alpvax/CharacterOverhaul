package alpvax.characteroverhaul.api.ability;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.Reference;
import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

/**
 * Override one or more of {@link #onActiveTick}, {@link #onInactiveTick}, {@link #onTrigger}, {@link #onReset} for this
 * to actually do something.
 * @author Alpvax
 */
public abstract class Ability extends IForgeRegistryEntry.Impl<Ability>
{
	public enum InputAllowed
	{
		/** Does not accept user input */
		NONE,
		/** Accepts user input while not active */
		ACTIVATE,
		/** Accepts user input while active or inactive */
		TOGGLE;
	}

	private final InputAllowed inputAllowed;

	public Ability(String id, InputAllowed input)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of ability \"%s\" with no id", toString());
		setRegistryName(id);
		inputAllowed = input == null ? InputAllowed.NONE : input;
	}

	public String getLocalisedName()
	{
		ResourceLocation r = getRegistryName();
		return I18n.format(String.format("%s:ability.%s.name", r.getResourceDomain(), r.getResourcePath()));
	}

	public void onInput(AbilityInstance inst)
	{
		if(inputAllowed == InputAllowed.NONE)
		{
			return;
		}
		if(!inst.isActive())
		{
			inst.trigger();
		}
		else if(inputAllowed == InputAllowed.TOGGLE)
		{
			inst.reset();
		}
	}

	public boolean canToggle(AbilityInstance inst)
	{
		return inst.getTicksSinceStateChange() < getCooldown(inst);
	}

	public int getCooldown(AbilityInstance inst)
	{
		return 0;
	}

	protected void onActiveTick(AbilityInstance inst)
	{
	}

	protected void onInactiveTick(AbilityInstance inst)
	{
	}

	protected void onTrigger(AbilityInstance inst)
	{
	}

	protected void onReset(AbilityInstance inst)
	{
	}

	public abstract AbilityInstance createNewAbilityInstance(ICharacter character);

	/**
	 * Change this value in order to allow for more/fewer abilities.
	 */
	private static final int MAX_ABILITY_ID = 0xfff;

	public static final FMLControlledNamespacedRegistry<Ability> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(Reference.MOD_ID, "abilities"), Ability.class, null, 0, MAX_ABILITY_ID, true, null, null, null);
}
