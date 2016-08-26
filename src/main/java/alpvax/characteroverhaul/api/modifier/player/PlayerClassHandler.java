package alpvax.characteroverhaul.api.modifier.player;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.character.modifier.RegistryCharModHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;

public class PlayerClassHandler extends RegistryCharModHandler<PlayerClass>
{
	public PlayerClassHandler(ICharacter attached)
	{
		super(attached);
	}

	@Override
	public ResourceLocation getKey()
	{
		return CharacterOverhaulReference.MODIFIER_CLASS_KEY;
	}

	@Override
	protected FMLControlledNamespacedRegistry<PlayerClass> getRegistry()//Override to save trying to find and cast
	{
		return PlayerClass.REGISTRY;
	}

	@Override
	public boolean setModifier(PlayerClass modifier)
	{
		if(modifier != null)
		{
			return false;
		}
		return super.setModifier(modifier);
	}
}
