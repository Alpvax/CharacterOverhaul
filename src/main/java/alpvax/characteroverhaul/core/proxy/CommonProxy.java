package alpvax.characteroverhaul.core.proxy;

import java.util.function.Function;

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
		return ctx.getServerHandler().playerEntity;
	}

	/**
	 * <strong>ONLY CALL THIS METHOD ON THE CLIENT!</strong><br>
	 * Calls ClientObjectFactoryRegistry.create.<br>
	 * Throws a runtime error if called on the server.
	 * @return a new instance of the requested class.
	 */
	public <T> T createClientObject(String className, Class<T> objectType)
	{
		throw new RuntimeException("Tried calling client method createClientObject on server");
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

	public <T> void registerClientFactory(Class<T> objectType, String className, Function<ICharacter, T> factory)
	{}

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
