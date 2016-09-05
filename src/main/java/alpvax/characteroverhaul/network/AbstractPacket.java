package alpvax.characteroverhaul.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Copied from <a href=
 * "https://github.com/SlimeKnights/Mantle/blob/master/src/main/java/slimeknights/mantle/network/AbstractPacket.java">
 * Mantle</a>, so all credit goes to the developers of that library.
 */
public abstract class AbstractPacket implements IMessage
{
	public abstract IMessage handleClient(NetHandlerPlayClient netHandler);

	public abstract IMessage handleServer(NetHandlerPlayServer netHandler);

	protected void writePos(BlockPos pos, ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}

	protected BlockPos readPos(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		return new BlockPos(x, y, z);
	}

	/**
	 * Threadsafe integration of the abstract packet. Basically if you're doing something that has any influence on the
	 * world you should use this. (That's ~everything)
	 *
	 * Copied from <a href=
	 * "https://github.com/SlimeKnights/Mantle/blob/master/src/main/java/slimeknights/mantle/network/AbstractPacketThreadsafe.java">
	 * Mantle</a>, so all credit goes to the developers of that library.
	 */
	public static abstract class Threadsafe extends AbstractPacket
	{
		@Override
		public final IMessage handleClient(final NetHandlerPlayClient netHandler)
		{
			FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					handleClientSafe(netHandler);
				}
			});
			return null;
		}

		@Override
		public final IMessage handleServer(final NetHandlerPlayServer netHandler)
		{
			FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					handleServerSafe(netHandler);
				}
			});
			return null;
		}

		public abstract void handleClientSafe(NetHandlerPlayClient netHandler);

		public abstract void handleServerSafe(NetHandlerPlayServer netHandler);
	}
}
