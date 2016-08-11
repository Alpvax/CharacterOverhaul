package alpvax.characteroverhaul.api.settings;

public class Config
{
	public final Config deafults;

	public Config(Config parent)
	{
		this.deafults = parent;
	}

	protected int numAbilities = 4;

	public int getNumAbilities()
	{
		return numAbilities;
	}
}
