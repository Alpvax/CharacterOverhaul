package alpvax.characteroverhaul.api.character.modifier;

import java.util.List;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.client.gui.ICharacterCreationPage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class CharacterModifierFactory<T extends ICharacterModifierHandler<?>> extends IForgeRegistryEntry.Impl<CharacterModifierFactory<?>>
{
	public CharacterModifierFactory(String key)
	{
		setRegistryName(key);
	}

	public CharacterModifierFactory(ResourceLocation key)
	{
		setRegistryName(key);
	}

	protected abstract boolean isValidForCharacter(ICharacter character);

	protected abstract T newHandler(ICharacter character);

	@SideOnly(Side.CLIENT)
	public abstract List<ICharacterCreationPage> getPagesForGUI();
}
