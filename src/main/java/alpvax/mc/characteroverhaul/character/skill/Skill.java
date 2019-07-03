package alpvax.mc.characteroverhaul.character.skill;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Skill {
  public final ResourceLocation id;
  public final IAttribute xpModifierAttribute;
  private int maxLevel;
  private Function<Integer, Float> xpGetter;

  public Skill(ResourceLocation id, int maxLevel, Function<Integer, Float> xpForLevelGetter) {
    this.id = id;
    this.xpModifierAttribute = (new Attribute(
        null,
        CharacterOverhaul.MODID + ".skill.xpmodifier.attribute." + id.toString(),
        1D
    ) {
      @Override
      public double clampValue(double value) {
        return value;
      }
    }
    );
    this.maxLevel = maxLevel;
    this.xpGetter = xpForLevelGetter;
  }

  public Skill(ResourceLocation id, List<Float> xpRequirements) {
    this(id, xpRequirements.size(), xpRequirements::get);
  }

  public Skill(ResourceLocation id, float[] xpRequirements) {
    this(id, xpRequirements.length, l -> xpRequirements[l]);
  }

  public int getMaxLevel() {
    return maxLevel;
  }

  public float getXPForLevel(int level) {
    Preconditions.checkElementIndex(level, getMaxLevel(), "XP level");
    return xpGetter.apply(level);
  }

  static final Map<ResourceLocation, Skill> SKILLS = Maps.newHashMap();

  public static Skill get(ResourceLocation rl) {
    return SKILLS.get(rl);
  }
  /*public static final Map<String, IXPLevelGetterFactory> levelGetterFactories = Maps.newHashMap();

  public static interface IXPLevelGetterFactory<T> {
    Function<Integer, Float> read(JsonObject json);
    /*Function<Integer, Float> read(PacketBuffer buffer);
    void write(PacketBuffer buffer);
  }*/
}
