package alpvax.characteroverhaul.api.character.modifier;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

public abstract class RegistryCharModFactory<T extends ICharacterModifierHandler<M>, M extends ICharacterModifier & IForgeRegistryEntry<M>> extends CharacterModifierFactory<T, M>
{
	private final FMLControlledNamespacedRegistry<M> registry;

	public RegistryCharModFactory(ResourceLocation key, Class<M> modifierClass, int maxId)
	{
		super(key);
		registry = PersistentRegistryManager.createRegistry(getRegistryName(), modifierClass, null, 0, maxId, true, null, null, null);
	}

	public RegistryCharModFactory(ResourceLocation key, FMLControlledNamespacedRegistry<M> registry)
	{
		super(key);
		this.registry = registry;
	}

	public FMLControlledNamespacedRegistry<M> registry()
	{
		return registry;
	}

	@Override
	protected M getDefaultModifier(ICharacter character)
	{
		return registry().getDefaultValue();
	}
}
