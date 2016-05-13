package alpvax.characteroverhaul.api.character;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICharacterModifierExtended extends ICharacterModifier, INBTSerializable<NBTTagCompound>
{
	/**
	 * Whether the data should be saved to the character (e.g. perk-granted modifiers) or not (e.g. ItemStack modifiers
	 * should save to the itemstack instead).
	 * @return
	 */
	public default boolean shouldSaveNBTToCharacter()
	{
		return true;
	}
}
