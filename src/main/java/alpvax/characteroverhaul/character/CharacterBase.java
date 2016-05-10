package alpvax.characteroverhaul.character;

import java.util.HashMap;
import java.util.Map;

import alpvax.characteroverhaul.perk.Perk;
import net.minecraft.util.ResourceLocation;

public class CharacterBase implements ICharacter
{
	private Map<ResourceLocation, Integer> perks = new HashMap<>();

	@Override
	public int getPerkLevel(Perk perk)
	{
		Integer i = perks.get(perk.getRegistryName());
		return i != null ? i.intValue() : 0;
	}

	@Override
	public void setPerkLevel(Perk perk, int level)
	{
		perks.put(perk.getRegistryName(), Integer.valueOf(level));
		perk.onLevelChange(level, this);
	}

}
