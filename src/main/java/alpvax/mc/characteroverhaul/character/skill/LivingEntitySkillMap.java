package alpvax.mc.characteroverhaul.character.skill;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
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
    levels.put(skill, res.getLeft());
    xp.put(skill, res.getRight());
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
      levels.put(skill, tag.getInt("level"));
      xp.put(skill, tag.getFloat("xp"));
    });
  }
}
