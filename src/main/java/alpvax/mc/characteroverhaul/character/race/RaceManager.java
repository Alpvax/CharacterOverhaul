package alpvax.mc.characteroverhaul.character.race;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class RaceManager extends JsonReloadListener {
  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
  private static final Logger LOGGER = LogManager.getLogger();

  public RaceManager() {
    super(GSON, CharacterOverhaul.MODID + "/races");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonObject> jsons, IResourceManager resourceManager, IProfiler profiler) {
    jsons.forEach((loc, json) -> {
      LOGGER.info("Found race description: {}\n\t{}", loc, json);
    });
  }
}
