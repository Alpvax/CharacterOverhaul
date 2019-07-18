package alpvax.mc.characteroverhaul.character;

import alpvax.mc.characteroverhaul.character.modifier.ICharacterModifierSource;
import alpvax.mc.characteroverhaul.character.race.IRace;
import alpvax.mc.characteroverhaul.character.skill.ISkillMap;
import alpvax.mc.characteroverhaul.character.skill.LivingEntitySkillMap;
import com.google.common.base.Preconditions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nonnull;

public class LivingEntityCharacter implements ICharacter {
  private LivingEntity entity;
  private final ISkillMap skills;
  private IRace race;

  public LivingEntityCharacter(@Nonnull LivingEntity entity) {
    this.entity = entity;
    this.skills = new LivingEntitySkillMap(entity);
  }

  @Override
  public LivingEntity getAttached() {
    return Preconditions.checkNotNull(entity, "LivingEntityCharacter entity has not been set");
  }

  @Override
  public ISkillMap getSkills() {
    return skills;
  }

  @Override
  public void addModifierSource(ICharacterModifierSource source) {
    throw new NotImplementedException("TODO: Implement character modifiers");
  }

  @Override
  public boolean removeModifierSource(ICharacterModifierSource source) {
    throw new NotImplementedException("TODO: Implement character modifiers");//return false;
  }

  @Override
  public IRace getRace() {
    return race;//TODO: Implement character races
  }

  @Override
  public boolean setRace(IRace race) {
    /*throw new NotImplementedException("TODO: Implement character races");/*/return false;
  }
}
