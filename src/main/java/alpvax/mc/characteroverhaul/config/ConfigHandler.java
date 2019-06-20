package alpvax.mc.characteroverhaul.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHandler {
  public static final ForgeConfigSpec CLIENT_SPEC;
  public static final ForgeConfigSpec SERVER_SPEC;
  static final ClientConfig CLIENT;
  static final ServerConfig SERVER;
  static {
    {
      final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
      CLIENT = specPair.getLeft();
      CLIENT_SPEC = specPair.getRight();
    }
    {
      final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
      SERVER = specPair.getLeft();
      SERVER_SPEC = specPair.getRight();
    }
  }

  // We store a reference to the ModConfigs here to be able to change the values in them from our code
  // (For example from a config GUI)
  private static ModConfig clientConfig;
  private static ModConfig serverConfig;

  public static void updateClient(final ModConfig config) {
    clientConfig = config;

    COConfig.displayAttributesGrouped = CLIENT.displayAsAttributes.get();
  }

  public static void updateServer(final ModConfig config) {
    serverConfig = config;


  }

  private static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
    modConfig.getConfigData().set(path, newValue);
    modConfig.save();
  }
}
