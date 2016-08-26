package alpvax.characteroverhaul.api.character.modifier;

import java.util.List;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class RegistryCharModHandler<T extends ICharacterModifier & IForgeRegistryEntry<T>> implements ICharacterModifierHandler<T>
{
	private final ICharacter character;
	private T modifier = null;

	public RegistryCharModHandler(ICharacter attached)
	{
		character = attached;
	}

	@Override
	public NBTTagString serializeNBT()
	{
		return new NBTTagString(modifier.getRegistryName().toString());
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		modifier = getRegistry().getObject(new ResourceLocation(((NBTTagString)nbt).getString()));
	}

	@SuppressWarnings("unchecked")
	protected FMLControlledNamespacedRegistry<T> getRegistry()
	{
		return ((RegistryCharModFactory<?, T>)CharacterModifierFactory.REGISTRY.getValue(getKey())).registry();
	}

	@Override
	public ICharacter getCharacter()
	{
		return character;
	}

	@Override
	public boolean setModifier(T modifier)
	{
		this.modifier = modifier;
		return true;
	}

	@Override
	public T getDefaultModifier()
	{
		return getRegistry().getDefaultValue();
	}

	@Override
	public List<PerkModifier> getPerkModifiers()
	{
		return modifier.getPerkModifiers();
	}
}
