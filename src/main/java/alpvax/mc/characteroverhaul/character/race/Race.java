package alpvax.mc.characteroverhaul.character.race;

import alpvax.mc.characteroverhaul.character.modifier.ICharacterModifier;
import alpvax.mc.characteroverhaul.character.modifier.ICharacterModifierSource;
import alpvax.mc.characteroverhaul.character.modifier.ICharacterModifierType;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  public ITextComponent displayName() {
    return null;
  }

  @Override
  public CompoundNBT serializeNBT() {
    return null;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {

  }

  static class Builder {
    private ResourceLocation name;
    private final List<ICharacterModifier> modifiers = new ArrayList<>();

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
  }
}
