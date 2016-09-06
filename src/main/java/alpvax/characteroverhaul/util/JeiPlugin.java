package alpvax.characteroverhaul.util;

import java.awt.Rectangle;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.renderer.InventoryEffectRenderer;

@JEIPlugin
public class JeiPlugin implements IModPlugin
{

	@Override
	public void register(IModRegistry registry)
	{
		registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler<InventoryEffectRenderer>()
		{

			@Override
			public Class<InventoryEffectRenderer> getGuiContainerClass()
			{
				return InventoryEffectRenderer.class;
			}

			@Override
			public List<Rectangle> getGuiExtraAreas(InventoryEffectRenderer guiContainer)
			{
				return /*TODO:if tabs enabled ?*/ Lists.newArrayList(new Rectangle(/*TODO:Get Rect for eact tab*/));// : null;
			}
		});
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
		// TODO Auto-generated method stub

	}

}
