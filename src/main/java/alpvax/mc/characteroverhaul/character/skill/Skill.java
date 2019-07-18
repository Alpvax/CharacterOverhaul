package alpvax.mc.characteroverhaul.character.skill;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import alpvax.mc.characteroverhaul.character.ICharacter;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class Skill extends ForgeRegistryEntry<Skill> {
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
    //noinspection ResultOfMethodCallIgnored
    Preconditions.checkElementIndex(level, getMaxLevel(), "XP level");
    return xpGetter.apply(level);
  }

  public ITextComponent getName() {
    return new TranslationTextComponent(Util.makeTranslationKey("skill", id));
  }

  static final Map<ResourceLocation, Skill> SKILLS = Maps.newHashMap();

  public static Skill get(ResourceLocation rl) {
    return SKILLS.get(rl);
  }

  /**
   * Called when the character level is set to something different to what it was previously (e.g. gained enough xp to
   * level up, or when the character is loaded).
   *
   * @param character the character whose level is changing. It has not changed at this point, so the old level can be
   *                  retrieved using {@link ICharacter#getSkillLevel}.
   */

  @SuppressWarnings("EmptyMethod")
  public void onLevelChange(ICharacter character) {

  }
  /*public static final Map<String, IXPLevelGetterFactory> levelGetterFactories = Maps.newHashMap();

  public static interface IXPLevelGetterFactory<T> {
    Function<Integer, Float> read(JsonObject json);
    /*Function<Integer, Float> read(PacketBuffer buffer);
    void write(PacketBuffer buffer);
  }*/
}
