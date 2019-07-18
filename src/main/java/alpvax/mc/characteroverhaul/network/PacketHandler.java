package alpvax.mc.characteroverhaul.network;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
  private static final String PROTOCOL_VERSION = Integer.toString(1);
  private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
      .named(new ResourceLocation(CharacterOverhaul.MODID, "main_channel"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();

  public static void register()
  {
    int index = 0;

    //HANDLER.registerMessage(index++, KeyPressPKT.class, KeyPressPKT::encode, KeyPressPKT::decode, KeyPressPKT.Handler::handle);
  }

  /**
   * Sends a packet to the server.<br>
   * Must be called Client side.
   */
  public static void sendToServer(Object msg)
  {
    HANDLER.sendToServer(msg);
  }

  /**
   * Send a packet to a specific player.<br>
   * Must be called Server side.
   */
  public static void sendTo(Object msg, ServerPlayerEntity player)
  {
    if (!(player instanceof FakePlayer))
    {
      HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }
  }
}
