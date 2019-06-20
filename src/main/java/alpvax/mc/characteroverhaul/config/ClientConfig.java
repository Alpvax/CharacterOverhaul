package alpvax.mc.characteroverhaul.config;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import net.minecraftforge.common.ForgeConfigSpec;

final class ClientConfig {
  final ForgeConfigSpec.BooleanValue displayAsAttributes;

  ClientConfig(final ForgeConfigSpec.Builder builder) {
    builder.push("general");
    displayAsAttributes = builder
        .comment("Display the attributes grouped by attribute, rather than by their source")
        .translation(CharacterOverhaul.MODID + ".config.displayAsAttributes")
        .define("clientBoolean", true);

    builder.pop();
  }
}
