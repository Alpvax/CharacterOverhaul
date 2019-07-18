package alpvax.mc.characteroverhaul.character.attribute;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import alpvax.mc.characteroverhaul.character.attribute.source.IAttributeModifierSource;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class SortedModifierMap {
  public static final Set<Supplier<Collection<IAttributeModifierSource>>> SOURCE_FACTORIES = Sets.newHashSet();
  /*static {
    EquipmentAttModSource.register();
    PotionAttModSource.register();
  }*/

  private final Multimap<IAttribute, ITextComponent> attMap = HashMultimap.create();
  private final Multimap<IAttributeModifierSource, ITextComponent> sourceMap = HashMultimap.create();

  public SortedModifierMap(LivingEntity entity) {
    Multimap<IAttribute, AttributeModifier> remaining = HashMultimap.create();
    entity.getAttributes().getAllAttributes().forEach(inst -> {
      remaining.putAll(inst.getAttribute(),inst.getModifiers());
    });
    SOURCE_FACTORIES.forEach(factory -> factory.get().forEach(source -> {
        source.getModifiers(entity).entries().forEach(e -> {
          put(e.getKey(), source, entity, e.getValue());
          remaining.remove(e.getKey(), e.getValue());
        });
    }));
    remaining.entries().forEach(e ->
        put(e.getKey(), null, entity, e.getValue())
    );
  }

  private void put(IAttribute attribute, IAttributeModifierSource source, LivingEntity entity, AttributeModifier modifier) {
    if (modifier.getAmount() != 0.0D) {
      if (source == null) {
        source = new UnknownAttModSource(modifier.getName());
      }
      ITextComponent attName = AttributeDisplayUtil.getAttributeName(attribute);
      ITextComponent sourceName = new TranslationTextComponent(CharacterOverhaul.MODID + ".attributesource.from", source.getName(entity));
      attMap.put(attribute, AttributeDisplayUtil.getAttributeModifierText(modifier, sourceName));
      sourceMap.put(source, AttributeDisplayUtil.getAttributeModifierText(modifier, attName));
    }
  }

  public int size() {
    return attMap.size();
  }

  public boolean isEmpty() {
    return attMap.isEmpty();
  }

  public Multimap<IAttribute, ITextComponent> groupByAttribute() {
    return attMap;
  }

  public Multimap<IAttributeModifierSource, ITextComponent> groupBySource() {
    return sourceMap;
  }

  private static class UnknownAttModSource implements IAttributeModifierSource {
    private String source;
    UnknownAttModSource(String source) { this.source = source; }
    @Override
    public Type getType() { return null; }

    @Override
    public ITextComponent getName(LivingEntity entity) {
      return new TranslationTextComponent(CharacterOverhaul.MODID + ".attributesource.unknown", source);
    }

    @Override
    public Multimap<IAttribute, AttributeModifier> getModifiers(LivingEntity entity) { return null; }

    @Override
    public boolean equals(Object other) {
      return other instanceof UnknownAttModSource && ((UnknownAttModSource) other).source.equals(source);
    }

    @Override
    public int hashCode() {
      return Objects.hash("UnknownAttModSource", source);
    }
  }
}
