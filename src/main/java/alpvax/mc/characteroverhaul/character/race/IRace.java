package alpvax.mc.characteroverhaul.character.race;

import net.minecraft.command.arguments.NBTTagArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRace extends INBTSerializable<CompoundNBT> {
  ITextComponent displayName();


}
