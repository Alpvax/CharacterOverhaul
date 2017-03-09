package alpvax.characteroverhaul.core.proxy;

import java.util.function.Function;

import javax.annotation.Nonnull;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener
{
	@Override
	public void registerPre(FMLPreInitializationEvent event)
	{
		((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		//TODO
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return(ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}

	@Override
	public <T> T createClientObject(@Nonnull String className, @Nonnull Class<T> objectType)
	{
		return ClientObjectFactoryRegistry.create(className, objectType, getClientCharacter());
	}

	@Override
	public ICharacter getClientCharacter()
	{
		return Minecraft.getMinecraft().player.getCapability(ICharacter.CAPABILITY, null);
	}

	@Override
	public <T> void registerClientFactory(Class<T> objectType, String className, Function<ICharacter, T> factory)
	{
		ClientObjectFactoryRegistry.addFactory(objectType, className, factory);
	}

	/*private final List<ICharacterCreationPageHandler> creationGuiPages = new ArrayList<>();
	
	@Override
	public List<ICharacterCreationPageHandler> getPageHandlers()
	{
		return Collections.unmodifiableList(creationGuiPages);
	}
	
	@Override
	public void registerCreationGUIHandler(Class<?> clazz)
	{
		if(ICharacterCreationPageHandler.class.isAssignableFrom(clazz))
		{
			try
			{
				creationGuiPages.add((ICharacterCreationPageHandler)clazz.newInstance());
			}
			catch(InstantiationException | IllegalAccessException e)
			{
				//TODO:Log "No-arg Constructor not found for class <clazz>"
			}
		}
		if(ICharacterCreationPage.class.isAssignableFrom(clazz))
		{
			creationGuiPages.add(new ICharacterCreationPageHandler()
			{
				@Override
				public List<ICharacterCreationPage> getPages(ICharacter character)
				{
					try
					{
						return Lists.newArrayList((ICharacterCreationPage)clazz.newInstance());
					}
					catch(InstantiationException | IllegalAccessException e)
					{
						//TODO:Log "No-arg Constructor not found for class <clazz>"
						return null;
					}
				}
			});
		}
	}
	
	/*@Override
	public void registerTabGUIHandler(IMCMessage message)
	{
		Function<GuiScreen, Result> func = message.getFunctionValue(GuiScreen.class, Result.class).orNull();
		if(func != null)
		{
			GuiCharacterTabHelper.registerGuiHandler(func);
		}
	}
	
	@Override
	public void registerTabLayoutHandler(IMCMessage message)
	{
		Function<GuiScreen, GuiCharacterTabHelper> factory = message.getFunctionValue(GuiScreen.class, GuiCharacterTabHelper.class).orNull();
		if(factory != null)
		{
			GuiCharacterTabHelper.registerGuiHandler(factory);
		}
	}*/
}
