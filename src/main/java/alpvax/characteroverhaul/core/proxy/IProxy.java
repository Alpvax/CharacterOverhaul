package alpvax.characteroverhaul.core.proxy;

import java.util.List;

import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPageHandler;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;

public interface IProxy
{
	public default void registerPre()
	{}

	public default void registerCreationGUIHandler(Class<?> pageHandler)
	{}

	public default List<ICharacterCreationPageHandler> getPageHandlers()
	{
		return null;
	}

	public default void registerTabGUIHandler(IMCMessage message)
	{}

	public default void registerTabLayoutHandler(IMCMessage message)
	{}
}
