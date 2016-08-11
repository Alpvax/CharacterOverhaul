package alpvax.characteroverhaul.core;

import alpvax.characteroverhaul.api.settings.Config;

public class ModifiableConfig extends Config
{
	public ModifiableConfig(Config parent)
	{
		super(parent);
	}

	public void setNumAbilities(int num)
	{
		numAbilities = num;
	}

}
