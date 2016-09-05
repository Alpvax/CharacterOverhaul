package alpvax.characteroverhaul.network;

import java.util.List;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Modified based upon the one from <a href=
 * "https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/slimeknights/tconstruct/common/config/ConfigSyncPacket.java">
 * TConstruct</a>, so all credit goes to the developers of that mod.
 */
public class ConfigSyncPacket extends AbstractPacket
{

	public List<ConfigCategory> categories = Lists.newLinkedList();

	public ConfigSyncPacket()
	{
	}

	@Override
	public IMessage handleClient(NetHandlerPlayClient netHandler)
	{
		//TODO:ConfigSync.syncConfig(categories); See "https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/slimeknights/tconstruct/common/config/ConfigSync.java"
		return null;
	}

	@Override
	public IMessage handleServer(NetHandlerPlayServer netHandler)
	{
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		short categoryCount = buf.readShort();
		for(short i = 0; i < categoryCount; i++)
		{
			int propCount = buf.readInt();
			String categoryName = ByteBufUtils.readUTF8String(buf);
			ConfigCategory category = new ConfigCategory(categoryName);
			categories.add(category);
			for(int j = 0; j < propCount; j++)
			{
				String name = ByteBufUtils.readUTF8String(buf);
				char type = buf.readChar();
				String value = ByteBufUtils.readUTF8String(buf);
				category.put(name, new Property(name, value, Property.Type.tryParse(type)));
			}
		}

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeShort(categories.size());
		for(ConfigCategory category : categories)
		{
			buf.writeInt(category.values().size());
			ByteBufUtils.writeUTF8String(buf, category.getName());
			for(Property prop : category.values())
			{
				ByteBufUtils.writeUTF8String(buf, prop.getName());
				buf.writeChar(prop.getType().getID());
				ByteBufUtils.writeUTF8String(buf, prop.getString()); // always has string representation of the value
			}
		}
	}
}
