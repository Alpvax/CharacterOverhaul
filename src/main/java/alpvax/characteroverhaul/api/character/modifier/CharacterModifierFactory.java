package alpvax.characteroverhaul.api.character.modifier;

import java.util.List;

import alpvax.characteroverhaul.api.CharacterOverhaulReference;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class CharacterModifierFactory<T extends ICharacterModifierHandler<M>, M extends ICharacterModifier> extends IForgeRegistryEntry.Impl<CharacterModifierFactory<?, ?>>
{
	/**
	 * Change this value in order to allow for more/fewer perks.
	 */
	private static final int MAX_MODIFIER_ID = 0xff;

	@SuppressWarnings("unchecked")
	public static final FMLControlledNamespacedRegistry<CharacterModifierFactory<?, ?>> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(CharacterOverhaulReference.MOD_ID, "modiferhandlers"), CharacterModifierFactory.class, null, 0, MAX_MODIFIER_ID, true, null, null, null);

	public CharacterModifierFactory(String key)
	{
		setRegistryName(key);
	}

	public CharacterModifierFactory(ResourceLocation key)
	{
		setRegistryName(key);
	}

	public final T getModifierHandler(ICharacter character)
	{
		return character.getModifierHandler(getRegistryName());
	}

	public abstract boolean isValidForCharacter(ICharacter character);

	public T newHandler(ICharacter character)
	{
		T handler = createHandler(character);
		handler.setModifier(getDefaultModifier(character));
		return handler;
	}

	protected abstract T createHandler(ICharacter character);

	protected abstract M getDefaultModifier(ICharacter character);

	@SideOnly(Side.CLIENT)
	public abstract List<ICharacterCreationPage> getPagesForGUI();
}
