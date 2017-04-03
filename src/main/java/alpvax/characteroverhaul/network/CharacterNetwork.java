package alpvax.characteroverhaul.network;

import alpvax.characteroverhaul.core.CharacterOverhaulMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Modified version of TConstruct's <a href=
 * "https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/slimeknights/tconstruct/common/TinkerNetwork.java">
 * TinkerNetwork</a>, so all credit goes to those guys.
 */
public class CharacterNetwork extends NetworkWrapper
{
	public static CharacterNetwork instance = new CharacterNetwork();

	public CharacterNetwork()
	{
		super(CharacterOverhaulMod.MOD_ID);
	}

	public static void init()
	{
		// register all the packets
		instance.registerPacketClient(ConfigSyncPacket.class);

		instance.registerPacket(AbilityTriggerPacket.class);

		instance.registerPacketClient(AbilityChangedPacket.class);
		instance.registerPacketClient(AbilityEquippedPacket.class);
	}

	public static void sendToAll(AbstractPacket packet)
	{
		instance.network.sendToAll(packet);
	}

	public static void sendTo(AbstractPacket packet, EntityPlayerMP player)
	{
		instance.network.sendTo(packet, player);
	}


	public static void sendToAllAround(AbstractPacket packet, NetworkRegistry.TargetPoint point)
	{
		instance.network.sendToAllAround(packet, point);
	}

	public static void sendToDimension(AbstractPacket packet, int dimensionId)
	{
		instance.network.sendToDimension(packet, dimensionId);
	}

	public static void sendToServer(AbstractPacket packet)
	{
		instance.network.sendToServer(packet);
	}

	public static void sendToClients(WorldServer world, BlockPos pos, AbstractPacket packet)
	{
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		for(EntityPlayer player : world.playerEntities)
		{
			// only send to relevant players
			if(!(player instanceof EntityPlayerMP))
			{
				continue;
			}
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			if(world.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, chunk.xPosition, chunk.zPosition))
			{
				CharacterNetwork.sendTo(packet, playerMP);
			}
		}
	}
}
