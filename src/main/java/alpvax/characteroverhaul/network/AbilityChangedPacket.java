package alpvax.characteroverhaul.network;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.core.CharacterOverhaulMod;
import alpvax.characteroverhaul.core.util.ObjectFactoryRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AbilityChangedPacket extends AbstractPacket
{
	private UUID id;
	private String abilityClassName;
	private NBTTagCompound nbt;

	public AbilityChangedPacket()
	{}

	public AbilityChangedPacket(@Nonnull UUID id, @Nullable Ability ability)
	{
		this.id = id;
		if(ability == null)
		{
			abilityClassName = "";
			nbt = null;
		}
		else
		{
			abilityClassName = ability.getClass().getName();
			nbt = ability.getNBTForClientSync();
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		id = new UUID(buf.readLong(), buf.readLong());
		abilityClassName = ByteBufUtils.readUTF8String(buf);
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
		ByteBufUtils.writeUTF8String(buf, abilityClassName);
		ByteBufUtils.writeTag(buf, nbt);
	}

	@Override
	public IMessage handleClient(NetHandlerPlayClient netHandler)
	{
		FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				if(abilityClassName.length() > 0)
				{
					ICharacter character = CharacterOverhaulMod.proxy.getClientCharacter();
					Ability a = ObjectFactoryRegistry.create(abilityClassName, Ability.class, character);
					if(nbt != null)
					{
						a.readClientData(nbt);
					}
					character.updateAbilityClient(id, a);
				}
			}
		});
		return null;
	}
	
	@Override
	public IMessage handleServer(NetHandlerPlayServer netHandler)
	{
		throw new RuntimeException("AbilityChangedPacket received on server! How did this happen!");
	}
}
