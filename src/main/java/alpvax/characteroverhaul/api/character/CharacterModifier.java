package alpvax.characteroverhaul.api.character;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class CharacterModifier
{
	private Map<IAttribute, Set<AttributeModifier>> attributes = new HashMap<>();
	//TODO:private Set<IAbilityProvider> abilities = new HashSet<>();
	private UUID id = UUID.randomUUID();//TODO:Correct UUID
	private boolean persist = true;

	public CharacterModifier addAttributeModifier(IAttribute attribute, AttributeModifier modifier)
	{
		Set<AttributeModifier> set = attributes.get(attribute);
		if(set == null)
		{
			set = new HashSet<>();
			attributes.put(attribute, set);
		}
		set.add(modifier);
		return this;
	}

	public void apply(ICharacter character)
	{
		AbstractAttributeMap abilityMap = character.getAttributeMap();
		if(abilityMap != null)
		{
			for(Map.Entry<IAttribute, Set<AttributeModifier>> e : attributes.entrySet())
			{
				IAttributeInstance att = abilityMap.getAttributeInstance(e.getKey());
				if(att != null)
				{
					for(AttributeModifier am : e.getValue())
					{
						att.applyModifier(am);
					}
				}
			}
		}
		//TODO:Add abilities to character.
		//TODO:character.markdirty
	}

	public void remove(ICharacter character)
	{
		AbstractAttributeMap abilityMap = character.getAttributeMap();
		if(abilityMap != null)
		{
			for(Map.Entry<IAttribute, Set<AttributeModifier>> e : attributes.entrySet())
			{
				IAttributeInstance att = abilityMap.getAttributeInstance(e.getKey());
				if(att != null)
				{
					for(AttributeModifier am : e.getValue())
					{
						att.removeModifier(am);
					}
				}
			}
		}
		//TODO:Remove abilities from character.
		//TODO:character.markdirty
	}

	public UUID getID()
	{
		return id;
	}

	public CharacterModifier setDeathPersist(boolean flag)
	{
		persist = flag;
		return this;
	}

	public boolean persistAcrossDeath()
	{
		return persist;
	}
}
