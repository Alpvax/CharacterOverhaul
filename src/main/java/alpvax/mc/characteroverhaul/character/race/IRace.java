package alpvax.mc.characteroverhaul.character.race;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRace {
  ResourceLocation id();
  ITextComponent displayName();


}
