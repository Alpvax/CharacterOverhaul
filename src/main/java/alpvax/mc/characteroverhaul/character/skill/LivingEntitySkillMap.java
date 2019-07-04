package alpvax.mc.characteroverhaul.character.skill;

import alpvax.mc.characteroverhaul.character.capability.CharacterCapability;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public class LivingEntitySkillMap implements ISkillMap {
  private Object2IntMap<Skill> levels = new Object2IntOpenHashMap<>();
  private Object2FloatMap<Skill> xp = new Object2FloatOpenHashMap<>();
  private LivingEntity entity;

  public LivingEntitySkillMap(LivingEntity entity) {
    this.entity = entity;
    Skill.SKILLS.values().forEach(skill -> entity.getAttributes().registerAttribute(skill.xpModifierAttribute));
  }

  @Override
  public void addXP(Skill skill, float amount) {
    Pair<Integer, Float> res = ISkillMap.getXPUpdated(getLevel(skill), getXP(skill), (float) (amount * getXPMultiplier(skill)), skill::getXPForLevel);
    setLevel(skill, res.getLeft());
    xp.put(skill, res.getRight().intValue());
  }

  private void setLevel(Skill skill, int level) {
    int prev = levels.getOrDefault(skill, -1); // Get actual level, not computed. If non-existant, will return -1
    if (prev < 0 || level != prev) {
      //Should always be present, as it is attached to the charCapability
      entity.getCapability(CharacterCapability.CHARACTER_CAPABILITY).ifPresent(skill::onLevelChange);
    }
    levels.put(skill, level);
  }

  @Override
  public int getLevel(Skill skill) {
    return levels.computeIntIfAbsent(skill, s -> 0);
  }

  @Override
  public float getXP(Skill skill) {
    return xp.computeFloatIfAbsent(skill, s -> 0F);
  }

  @Override
  public double getXPMultiplier(Skill skill) {
    return entity.getAttribute(skill.xpModifierAttribute).getValue();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    Skill.SKILLS.values().forEach(skill -> {
      CompoundNBT tag = new CompoundNBT();
      tag.putInt("level", getLevel(skill));
      tag.putFloat("xp", getXP(skill));
      nbt.put(skill.id.toString(), tag);
    });
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    levels.clear();
    xp.clear();
    nbt.keySet().forEach(key -> {
      Skill skill = Skill.SKILLS.get(new ResourceLocation(key));
      CompoundNBT tag = nbt.getCompound(key);
      setLevel(skill, tag.getInt("level"));
      xp.put(skill, tag.getFloat("xp"));
    });
  }
}
