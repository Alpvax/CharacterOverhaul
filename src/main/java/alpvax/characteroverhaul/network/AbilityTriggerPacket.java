package alpvax.characteroverhaul.network;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.config.CharacterConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLLog;

public class AbilityTriggerPacket extends AbstractPacket.Threadsafe
{
	private int slot = -1;
	/** No effect serverside, set serverside to update client */
	private int cooldownForClient;

	public AbilityTriggerPacket()
	{}

	public AbilityTriggerPacket(int abilitySlot)
	{
		slot = abilitySlot;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		slot = buf.readInt();
		cooldownForClient = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(slot);
		buf.writeInt(cooldownForClient);
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient netHandler)
	{
		ICharacter character = Minecraft.getMinecraft().player.getCapability(ICharacter.CAPABILITY, null);
		if(character != null)
		{
			Ability ability = character.getHotbarAbilities()[slot];
			if(ability != null)
			{
				ability.clientTrigger(cooldownForClient);
			}
		}
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer netHandler)
	{
		if(slot < 0 || slot > CharacterConfig.numAbilities)
		{
			FMLLog.warning("Tried to trigger ability in slot %d, IndexOutOfBounds ( 0- %d).", slot, CharacterConfig.numAbilities);
			return;
		}
		ICharacter character = netHandler.player.getCapability(ICharacter.CAPABILITY, null);
		if(character != null)
		{
			Ability ability = character.getHotbarAbilities()[slot];
			if(ability != null && ability.hasManualTrigger() && ability.attemptTrigger())
			{
				cooldownForClient = ability.getCooldown();
				CharacterNetwork.sendTo(this, netHandler.player);
			}
		}
	}

}
