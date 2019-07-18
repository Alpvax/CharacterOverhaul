package alpvax.mc.characteroverhaul.util.attribute;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AttributeDisplayUtil {

  @Nonnull
  public static ITextComponent getAttributeName(IAttribute attribute) {
    return getAttributeName(attribute.getName());
  }
  @Nonnull
  public static ITextComponent getAttributeName(String attributeName) {
    return new TranslationTextComponent("attribute.name." + attributeName);
  }
  public static ITextComponent getAttributeSummary(IAttributeInstance instance) {
    return getAttributeSummary(instance.getAttribute().getName(), instance.getValue());
  }
  public static ITextComponent getAttributeSummary(String attributeName, double amount) {
    return new TranslationTextComponent(
        CharacterOverhaul.MODID + ".attributeinfo.summary",
        getAttributeName(attributeName),
        ItemStack.DECIMALFORMAT.format(amount)
    );
  }

  @Nullable
  public static ITextComponent getAttributeModifierText(AttributeModifier modifier, ITextComponent attributeOrSource) {
    double amount = modifier.getAmount();
    if (amount == 0.0D) {
      return null;
    }
    AttributeModifier.Operation operation = modifier.getOperation();
    String translationKey = "attribute.modifier.";
    TextFormatting colour = TextFormatting.BLUE;
    if (amount > 0.0D) {
      translationKey += "plus.";
    } else if (amount < 0.0D) {
      translationKey += "take.";
      amount *= -1.0D;
      colour = TextFormatting.RED;
    }
    if (operation != AttributeModifier.Operation.ADDITION) { // Display as percentage
      amount *= 100;
    }
    return (new TranslationTextComponent(translationKey + operation.getId(), ItemStack.DECIMALFORMAT.format(amount), attributeOrSource)).applyTextStyle(colour);
  }

  /*public static Multimap<ITextComponent, ITextComponent> parseModifiers(LivingEntity entity) {
    return parseModifiers(entity, false);
  }
  public static Multimap<ITextComponent, ITextComponent> parseModifiers(LivingEntity entity, boolean groupBySource) {
    SortedModifierMap modifierMap = new SortedModifierMap(entity);
    return groupBySource ? modifierMap.groupBySource() : modifierMap.groupByAttribute();
  }*/
}
