package alpvax.mc.characteroverhaul.character.skill;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ISkillMap extends INBTSerializable<CompoundNBT> {
  void addXP(Skill skill, float amount);

  int getLevel(Skill skill);

  float getXP(Skill skill);

  double getXPMultiplier(Skill skill);

  default void forEach(TriConsumer<Skill, Integer, Float> consumer) {
    Skill.SKILLS.values().forEach(skill -> consumer.accept(skill, getLevel(skill), getXP(skill)));
  }

  /**
   * @param currentLevel   The current level
   * @param currentXP      The amount of xp for this level (i.e. totalXP - xp for previous level)
   * @param additionalXP   The amount of xp to add
   * @param levelCapGetter A getter of level -> amount of exp required to level up
   * @return a Pair of <resulting level (minimum 0), currentXP for level (minimum 0)>
   */
  static Pair<Integer, Float> getXPUpdated(int currentLevel, float currentXP, float additionalXP, Function<Integer, Float> levelCapGetter) {
    float xp = currentXP + additionalXP;
    while (xp < 0 && currentLevel > 0) {
      xp += levelCapGetter.apply(--currentLevel);
    }
    while (xp > levelCapGetter.apply(currentLevel)) {
      xp -= levelCapGetter.apply(currentLevel++);
    }
    return Pair.of(currentLevel, Math.max(xp, 0));
  }
}
