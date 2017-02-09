package alpvax.characteroverhaul.network;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.config.CharacterConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLLog;

public class AbilityKeyTriggerPacket extends AbstractPacket.Threadsafe
{
	private int slot = -1;

	public AbilityKeyTriggerPacket()
	{}

	public AbilityKeyTriggerPacket(int abilitySlot)
	{
		slot = abilitySlot;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		buf.writeInt(slot);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		slot = buf.readInt();
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient netHandler)
	{
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer netHandler)
	{
		if(slot < 0 || slot > CharacterConfig.numAbilities)
		{
			FMLLog.warning("Tried to trigger ability in slot %d, IndexOutOfBounds ( 0- %d).", slot, CharacterConfig.numAbilities);
			return;
		}
		ICharacter character = netHandler.playerEntity.getCapability(ICharacter.CAPABILITY, null);
		if(character != null)
		{
			Ability ability = character.getHotbarAbilities().get(slot);
			if(ability != null && ability.hasKeybind())
			{
				ability.getKeybindTrigger().onKeyPressed();
			}
		}
	}

}
