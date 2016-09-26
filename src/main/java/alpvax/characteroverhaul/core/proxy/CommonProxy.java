package alpvax.characteroverhaul.core.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPage;
import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPageHandler;
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

	private final List<ICharacterCreationPageHandler> creationGuiPages = new ArrayList<>();

	public List<ICharacterCreationPageHandler> getPageHandlers()
	{
		return Collections.unmodifiableList(creationGuiPages);
	}

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
}
