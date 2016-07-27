package alpvax.characteroverhaul.core.proxy;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy
{
	public void registerPre()
	{
	}

	@SideOnly(Side.CLIENT)
	public final ClientProxy getClientSide()
	{
		return (ClientProxy)this;
	}
}
