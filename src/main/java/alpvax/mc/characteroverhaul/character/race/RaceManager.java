package alpvax.mc.characteroverhaul.character.race;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import alpvax.mc.characteroverhaul.character.modifier.AttributeCharModType;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RaceManager extends JsonReloadListener {
  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
  private static final Logger LOGGER = LogManager.getLogger();
  private static Map<ResourceLocation, IRace> races = ImmutableMap.of();
  public static IRace HUMAN = new Race.Builder(new ResourceLocation(CharacterOverhaul.MODID, "human")).build();

  public RaceManager() {
    super(GSON, CharacterOverhaul.MODID + "/races");
  }


  @Override
  protected void apply(@Nonnull Map<ResourceLocation, JsonObject> jsons, @Nonnull IResourceManager resourceManager, @Nonnull IProfiler profiler) {
    races = jsons.entrySet().stream().map(e -> {
      ResourceLocation loc = e.getKey();
      JsonObject json = e.getValue();
      LOGGER.info("Found race description: {}\n\t{}", loc, json);
      Race.Builder builder = new Race.Builder(loc);
      JsonObject attMods = json.getAsJsonObject("attributeModifiers");
      //builder.addModifier(new AttributeCharModType()
      return builder.build();
    }).filter(r -> r != null).collect(ImmutableMap.toImmutableMap(IRace::id, Function.identity()));
  }


  public static IRace deserialize(CompoundNBT raceNBT) {
    INBT id = raceNBT.get("id");
    IRace race = null;
    switch (id.getId()) {
      case Constants.NBT.TAG_STRING: //Single resourcelocation id
        race = races.get(new ResourceLocation(id.getString()));
        break;
      case Constants.NBT.TAG_LIST: //Merged race
        //TODO: Merged races
        break;
    }
    if (race == null) {
      race = HUMAN;
    }
    if (race instanceof INBTSerializable) {
      ((INBTSerializable)race).deserializeNBT(raceNBT.get("additionalData"));
    }
    return race;
  }

  public static INBT toNBT(IRace race) {
    //TODO: Merged races
    CompoundNBT nbt =  new CompoundNBT();
    nbt.putString("id", race.id().toString());
    if (race instanceof INBTSerializable) {
      nbt.put("additionalData", ((INBTSerializable)race).serializeNBT());
    }
    return nbt;
  }
}
