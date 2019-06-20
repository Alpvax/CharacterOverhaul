package alpvax.mc.characteroverhaul.character.attribute.source;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EquipmentAttModSource implements IAttributeModifierSource {
  private EquipmentSlotType slot;

  EquipmentAttModSource(EquipmentSlotType slot) {
    this.slot = slot;
  }
  @Override
  public Type getType() {
    return Type.TEMPORARY;
  }

  @Override
  public ITextComponent getName(LivingEntity entity) {
    String translationKey = CharacterOverhaul.MODID + ".attributesource.equipment." + slot.getName();
    return new TranslationTextComponent(translationKey,entity.getItemStackFromSlot(slot).getDisplayName());
  }

  @Override
  public Multimap<IAttribute, AttributeModifier> getModifiers(LivingEntity entity) {
    Multimap<IAttribute, AttributeModifier> result = HashMultimap.create();
    entity.getItemStackFromSlot(slot).getAttributeModifiers(slot).asMap().entrySet().forEach(e -> {
      result.putAll(getAttributeByName(entity, e.getKey()), e.getValue());
    });
    return result;
  }

  public static Supplier<Collection<IAttributeModifierSource>> factory() {
    return () -> Arrays.stream(EquipmentSlotType.values()).map(EquipmentAttModSource::new).collect(Collectors.toList());
  }
}
