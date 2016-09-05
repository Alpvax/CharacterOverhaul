package alpvax.characteroverhaul.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * A small network implementation/wrapper using AbstractPackets instead of IMessages. Instantiate in your mod class and
 * register your packets accordingly.
 *
 * Copied from <a href=
 * "https://github.com/SlimeKnights/Mantle/blob/master/src/main/java/slimeknights/mantle/network/NetworkWrapper.java">
 * Mantle</a>, so all credit goes to the developers of that library.
 */
public class NetworkWrapper
{
	public final SimpleNetworkWrapper network;
	protected final AbstractPacketHandler handler;
	private int id = 0;

	public NetworkWrapper(String channelName)
	{
		network = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		handler = new AbstractPacketHandler();
	}

	/**
	 * Packet will be received on both client and server side.
	 */
	public void registerPacket(Class<? extends AbstractPacket> packetClazz)
	{
		registerPacketClient(packetClazz);
		registerPacketServer(packetClazz);
	}

	/**
	 * Packet will only be received on the client side
	 */
	public void registerPacketClient(Class<? extends AbstractPacket> packetClazz)
	{
		registerPacketImpl(packetClazz, Side.CLIENT);
	}

	/**
	 * Packet will only be received on the server side
	 */
	public void registerPacketServer(Class<? extends AbstractPacket> packetClazz)
	{
		registerPacketImpl(packetClazz, Side.SERVER);
	}

	private void registerPacketImpl(Class<? extends AbstractPacket> packetClazz, Side side)
	{
		network.registerMessage(handler, packetClazz, id++, side);
	}

	public static class AbstractPacketHandler implements IMessageHandler<AbstractPacket, IMessage>
	{

		@Override
		public IMessage onMessage(AbstractPacket packet, MessageContext ctx)
		{
			if(ctx.side == Side.SERVER)
			{
				return packet.handleServer(ctx.getServerHandler());
			}
			else
			{
				return packet.handleClient(ctx.getClientHandler());
			}
		}
	}
}
