package alpvax.characteroverhaul.api.perk;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class RootPerk extends Perk
{
	public RootPerk(String id)
	{
		super(id, null);
	}

	@Override
	protected final Perk getParent()
	{
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public abstract boolean shouldDisplay();
}
