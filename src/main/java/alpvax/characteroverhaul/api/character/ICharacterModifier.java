package alpvax.characteroverhaul.api.character;

import java.util.Arrays;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;

public interface ICharacterModifier
{
	//TODO:Add Abilities and/or Effects
	//private Set<IAbilityProvider> abilities = new HashSet<>();

	/**
	 * Helper class to create a simple ICharacterModifier.
	 */
	public static class Builder
	{
		private final UUID id;
		private final boolean persist;
		private Multimap<String, AttributeModifier> attributes = HashMultimap.<String, AttributeModifier>create();
		public Builder(UUID id, boolean persistAcrossDeath)
		{
			this.id = id;
			persist = persistAcrossDeath;
		}

		public Builder addAttributeModifiers(IAttribute attribute, AttributeModifier... modifiers)
		{
			attributes.putAll(attribute.getAttributeUnlocalizedName(), Arrays.asList(modifiers));
			return this;
		}

		public ICharacterModifier build()
		{
			return new ICharacterModifier()
			{
				@Override
				public UUID getID()
				{
					return id;
				}

				@Override
				public boolean persistAcrossDeath()
				{
					return persist;
				}

				@Override
				public Multimap<String, AttributeModifier> getAttributeModifiers()
				{
					return HashMultimap.<String, AttributeModifier>create(attributes);
				}
			};
		}
	}

	/**
	 * Called any time this is attached to a character.<br>
	 * Override if you need to do something unusual when this is attached. The default implementation does nothing.
	 * @param character the character this was attached to.
	 */
	public default void onApplied(ICharacter character)
	{
	}

	/**
	 * Called any time this is removed from a character.<br>
	 * Override if you need to do something unusual when this is detached. The default implementation does nothing.
	 * @param character the character this was detached from.
	 */
	public default void onRemoved(ICharacter character)
	{
	}

	/**
	 * @return the UUID used when this modifier is attached to a character.
	 */
	public UUID getID();

	/**
	 * Does this stay attached to the new character upon death?
	 * @return
	 */
	public abstract boolean persistAcrossDeath();

	/**
	 * Get all the AttributeModifiers to be applied to/removed from the character on attach/detach.<br>
	 * If the character does not support the attributes system, this will not be called.<br>
	 * If the character does not have the IAttribute specified by the map key, the corresponding modifiers will be
	 * ignored.
	 * @return
	 */
	public Multimap<String, AttributeModifier> getAttributeModifiers();
}
