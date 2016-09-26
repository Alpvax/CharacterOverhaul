package alpvax.characteroverhaul.api.modifier.player;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;
import alpvax.characteroverhaul.api.character.modifier.RegistryCharModHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

public abstract class PlayerClass extends IForgeRegistryEntry.Impl<PlayerClass> implements ICharacterModifier
{
	public PlayerClass(String id)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of player class \"%s\" with no id", toString());
		setRegistryName(id);
	}

	@Override
	public boolean isValidForCharacter(ICharacter character)
	{
		return character.getAttachedObject() instanceof EntityPlayer;
	}

	public static class Handler extends RegistryCharModHandler<PlayerClass>
	{
		public Handler(ICharacter attached)
		{
			super(attached);
		}

		@Override
		public ResourceLocation getKey()
		{
			return CharacterOverhaulReference.MODIFIER_CLASS_KEY;
		}

		@Override
		protected IForgeRegistry<PlayerClass> getRegistry()//Override to save trying to find and cast
		{
			return REGISTRY;
		}

		@Override
		public boolean setModifier(PlayerClass modifier)
		{
			if(modifier == null)
			{
				return false;
			}
			return super.setModifier(modifier);
		}

		@Override
		public PlayerClass getDefaultModifier()
		{
			return null;
		}
	}

	/**
	 * Change this value in order to allow for more/fewer perks.
	 */
	private static final int MAX_CLASS_ID = 0xff;

	public static final IForgeRegistry<PlayerClass> REGISTRY = new RegistryBuilder<PlayerClass>().setName(CharacterOverhaulReference.MODIFIER_CLASS_KEY).setType(PlayerClass.class).setIDRange(0, MAX_CLASS_ID).create();
}
