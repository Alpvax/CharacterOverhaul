package alpvax.characteroverhaul.util;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JeiPlugin implements IModPlugin
{

	@Override
	public void register(IModRegistry registry)
	{
		/*registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler<InventoryEffectRenderer>()
		{

			@Override
			public Class<InventoryEffectRenderer> getGuiContainerClass()
			{
				return InventoryEffectRenderer.class;
			}

			@Override
			public List<Rectangle> getGuiExtraAreas(InventoryEffectRenderer guiContainer)
			{
				return !CharacterConfig.client.renderPotionsInInventory ? Lists.newArrayList(new Rectangle(/*TODO:Get Rect for eact tab/)) : null;
			}

			@Override
			public Object getIngredientUnderMouse(InventoryEffectRenderer guiContainer, int mouseX, int mouseY)
			{
				// TODO Auto-generated method stub
				return null;
			}
		});*/
	}
}
