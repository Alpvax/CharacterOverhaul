package alpvax.characteroverhaul.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfigMessage implements IMessage
{

	@Override
	public void fromBytes(ByteBuf buf)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		// TODO Auto-generated method stub

	}


	public class Handler implements IMessageHandler<ConfigMessage, IMessage>
	{

		@Override
		public IMessage onMessage(ConfigMessage message, MessageContext ctx)
		{
			// TODO Auto-generated method stub
			return null;
		}

	}
}
