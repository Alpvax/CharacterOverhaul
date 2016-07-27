package alpvax.characteroverhaul.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener
{
	@Override
	public void registerPre()
	{
		super.registerPre();
		((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		//TODO
	}
}
