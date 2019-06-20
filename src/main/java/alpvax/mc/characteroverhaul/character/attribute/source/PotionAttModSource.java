package alpvax.mc.characteroverhaul.character.attribute.source;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.RegistryManager;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PotionAttModSource implements IAttributeModifierSource {
  private final Effect effect;
  PotionAttModSource(Effect effect) { this.effect = effect; }

  @Override
  public Type getType() {
    return Type.TEMPORARY;
  }

  @Override
  public ITextComponent getName(LivingEntity entity) {
    EffectInstance instance = entity.getActivePotionEffect(effect);
    ITextComponent name = effect.getDisplayName();
    if (instance != null) {
      if (instance.getAmplifier() > 0) {
        name.appendText(" ").appendSibling(new TranslationTextComponent("enchantment.level." + (instance.getAmplifier() + 1)));
      }
      name = new TranslationTextComponent(CharacterOverhaul.MODID + ".attributesource.potion", name);
    }
    return name;
  }

  @Override
  public Multimap<IAttribute, AttributeModifier> getModifiers(LivingEntity entity) {
    Multimap<IAttribute, AttributeModifier> result = HashMultimap.create();
    EffectInstance instance = entity.getActivePotionEffect(effect);
    if (instance != null) {
      effect.getAttributeModifierMap().entrySet().forEach(e -> {
        IAttribute att = e.getKey();
        result.put(att, entity.getAttribute(att).getModifier(e.getValue().getID()));
      });
    }
    return result;
  }

  public static Supplier<Collection<IAttributeModifierSource>> factory() {
    return () -> RegistryManager.ACTIVE.getRegistry(Effect.class).getValues().stream().map(PotionAttModSource::new).collect(Collectors.toList());
  }
}
