package alpvax.characteroverhaul.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CharacterCreationEvent extends Event
{
	/*private final ICharacter character;
	private final Map<ResourceLocation, ICharacterModifierHandler<?>> handlers = Maps.newLinkedHashMap();
	private final Map<ResourceLocation, ICharacterModifierHandler<?>> view = Collections.unmodifiableMap(handlers);

	public CharacterCreationEvent(ICharacter c)
	{
		character = c;
	}

	/**
	 * Retrieves the character that is being created, Not much state is set.
	 */
	/*public ICharacter getCharacter()
	{
		return character;
	}

	/**
	 * Adds a modifier handler to be attached to this object. Keys MUST be unique, it is suggested that you set the
	 * domain to your mod ID.
	 * @param key The name of owner of this handler.
	 * @param handler The Modifier handler
	 */
	/*public void addModifier(ResourceLocation key, ICharacterModifierHandler<?> handler)
	{
		if(handlers.containsKey(key))
			throw new IllegalStateException("Duplicate Modifier Handler Key: " + key + " " + handler);
		this.handlers.put(key, handler);
	}

	/**
	 * A unmodifiable view of the capabilities that will be attached to this object.
	 */
	/*public Map<ResourceLocation, ICharacterModifierHandler<?>> getModifiers()
	{
		return view;
	}*/
}
