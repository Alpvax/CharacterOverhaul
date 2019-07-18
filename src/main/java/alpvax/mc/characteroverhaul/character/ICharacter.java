package alpvax.mc.characteroverhaul.character;

import alpvax.mc.characteroverhaul.character.modifier.ICharacterModifierSource;
import alpvax.mc.characteroverhaul.character.race.IRace;
import alpvax.mc.characteroverhaul.character.skill.ISkillMap;
import alpvax.mc.characteroverhaul.character.skill.Skill;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICharacter {
  ICapabilityProvider getAttached();

  ISkillMap getSkills();

  default int getSkillLevel(Skill skill) {
    return getSkills().getLevel(skill);
  }

  default float getXPForNextLevel(Skill skill) {
    return getSkills().getXP(skill);
  }

  default void addExperience(Skill skill, int amount) {
    getSkills().addXP(skill, amount);
  }

  @OnlyIn(Dist.CLIENT)
  default float getSkillLevelPercent(Skill skill) {
    return getSkillLevel(skill) / getXPForNextLevel(skill);
  }

  void addModifierSource(ICharacterModifierSource source);

  boolean removeModifierSource(ICharacterModifierSource source);

  IRace getRace();

  boolean setRace(IRace race);
}
