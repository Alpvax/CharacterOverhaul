package alpvax.mc.characteroverhaul.util.attribute.source;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.text.ITextComponent;
import com.google.common.collect.Multimap;

import javax.annotation.Nullable;

public interface IAttributeModifierSource {
  enum Type {
    PERMANENT, TEMPORARY
  }

  @Nullable default IAttribute getAttributeByName(LivingEntity entity, String name) {
    IAttributeInstance inst = entity.getAttributes().getAttributeInstanceByName(name);
    return inst == null ? null : inst.getAttribute();
  }

  Type getType();

  ITextComponent getName(LivingEntity entity);

  Multimap<IAttribute, AttributeModifier> getModifiers(LivingEntity entity);
}
