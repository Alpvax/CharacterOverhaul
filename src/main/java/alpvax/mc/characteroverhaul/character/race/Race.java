package alpvax.mc.characteroverhaul.character.race;

import alpvax.mc.characteroverhaul.character.modifier.ICharacterModifier;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class Race implements IRace {
  public final ResourceLocation id;
  private final ImmutableList<ICharacterModifier> modifiers;

  private Race(ResourceLocation name, ImmutableList.Builder<ICharacterModifier> modifiers) {
    this.id = name;
    this.modifiers = modifiers.build();
  }

  //@Override
  public List<ICharacterModifier> getModifiers() {
    return modifiers;
  }

  @Override
  public ResourceLocation id() {
    return id;
  }

  @Override
  public ITextComponent displayName() {
    return new TranslationTextComponent(Util.makeTranslationKey("race", id()));
  }

  static class Builder {
    private ResourceLocation name;
    private final ImmutableList.Builder<ICharacterModifier> modifiers = new ImmutableList.Builder<>();

    public Builder(ResourceLocation name) {
      this.name = name;
    }

    public void addModifier(ICharacterModifier modifier) {
      modifiers.add(modifier);
    }

    /*static Builder deserialise(JsonObject json, JsonDeserializationContext context) {
      Builder builder = new Builder()
          ICharacterModifierType.TYPES.entrySet().stream().filter(entry -> json.has(entry.getKey())).forEach((key, modifierType) ->);
      ResourceLocation resourcelocation = json.has("attributes") ? new ResourceLocation(JSONUtils.getString(json, "parent")) : null;
      DisplayInfo displayinfo = json.has("display") ? DisplayInfo.deserialize(JSONUtils.getJsonObject(json, "display"), context) : null;
      AdvancementRewards advancementrewards = JSONUtils.deserializeClass(json, "rewards", AdvancementRewards.EMPTY, context, AdvancementRewards.class);
      Map<String, Criterion> map = Criterion.criteriaFromJson(JSONUtils.getJsonObject(json, "criteria"), context);
    }*/

    public IRace build() {
      return new Race(name, modifiers);
    }
  }
}
