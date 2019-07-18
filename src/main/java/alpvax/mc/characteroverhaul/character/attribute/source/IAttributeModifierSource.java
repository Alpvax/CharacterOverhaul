package alpvax.mc.characteroverhaul.character.attribute.source;

import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.text.ITextComponent;

public interface IAttributeModifierSource {
  enum Type {
    PERMANENT, TEMPORARY;
  }

  default IAttribute getAttributeByName(LivingEntity entity, String name) {
    IAttributeInstance inst = entity.getAttributes().getAttributeInstanceByName(name);
    return inst != null ? inst.getAttribute() : null;
  }

  Type getType();

  ITextComponent getName(LivingEntity entity);

  Multimap<IAttribute, AttributeModifier> getModifiers(LivingEntity entity);
}
