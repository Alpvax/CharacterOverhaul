package alpvax.mc.characteroverhaul.character.attribute.source;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.text.ITextComponent;
import com.google.common.collect.Multimap;

public interface IAttributeModifierSource {
  public enum Type {
    PERMANENT, TEMPORARY;
  }

  default public IAttribute getAttributeByName(LivingEntity entity, String name) {
    return entity.getAttributes().getAttributeInstanceByName(name).getAttribute();
  }

  Type getType();

  ITextComponent getName(LivingEntity entity);

  Multimap<IAttribute, AttributeModifier> getModifiers(LivingEntity entity);
}
