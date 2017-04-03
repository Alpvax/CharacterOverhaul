package alpvax.characteroverhaul.network;

import java.util.UUID;

import alpvax.characteroverhaul.core.CharacterOverhaulMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AbilityEquippedPacket extends AbstractPacket
{
	private UUID abilityID;
	private int slot;

	public AbilityEquippedPacket()
	{}

	public AbilityEquippedPacket(UUID id, int i)
	{
		abilityID = id;
		slot = i;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		abilityID = new UUID(buf.readLong(), buf.readLong());
		slot = buf.readChar();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(abilityID.getMostSignificantBits());
		buf.writeLong(abilityID.getLeastSignificantBits());
		buf.writeChar(slot);
	}

	@Override
	public IMessage handleClient(NetHandlerPlayClient netHandler)
	{
		FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				CharacterOverhaulMod.proxy.getClientCharacter().setHotbarSlot(slot, abilityID);
			}
		});
		return null;
	}

	@Override
	public IMessage handleServer(NetHandlerPlayServer netHandler)
	{
		throw new RuntimeException("AbilityEquippedPacket recieved on server! How did this happen!");
	}

}
