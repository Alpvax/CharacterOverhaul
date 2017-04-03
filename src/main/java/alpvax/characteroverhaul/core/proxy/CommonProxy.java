package alpvax.characteroverhaul.core.proxy;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class CommonProxy
{
	public void registerPre(FMLPreInitializationEvent event)
	{}

	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return ctx.getServerHandler().player;
	}

	/**
	 * <strong>ONLY CALL THIS METHOD ON THE CLIENT!</strong><br>
	 * Calls Minecraft.player.getCapability(ICharacter).<br>
	 * Throws a runtime error if called on the server.
	 * @return the character for the client player.
	 */
	public ICharacter getClientCharacter()
	{
		throw new RuntimeException("Tried calling client method getClientCharacter on server");
	}

	public void registerCreationGUIHandler(Class<?> pageHandler)
	{}

	/*public default List<ICharacterCreationPageHandler> getPageHandlers()
	{
		return null;
	}

	public default void registerTabGUIHandler(IMCMessage message)
	{}

	public default void registerTabLayoutHandler(IMCMessage message)
	{}*/
}
