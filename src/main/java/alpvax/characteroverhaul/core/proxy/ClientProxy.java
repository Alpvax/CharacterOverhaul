package alpvax.characteroverhaul.core.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPage;
import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy, IResourceManagerReloadListener
{
	@Override
	public void registerPre()
	{
		((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		//TODO
	}

	private final List<ICharacterCreationPageHandler> creationGuiPages = new ArrayList<>();

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
