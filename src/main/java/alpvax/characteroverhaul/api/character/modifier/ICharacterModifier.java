package alpvax.characteroverhaul.api.character.modifier;

import java.util.List;
import java.util.Map;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.util.ResourceLocation;

public interface ICharacterModifier
{
	public boolean isValidForCharacter(ICharacter character);

	public void onAttach(ICharacter character);

	public void onDetach(ICharacter character);

	public List<PerkModifier> getPerkModifiers();

	public boolean isValidFor(ICharacter character, Map<ResourceLocation, ICharacterModifierHandler<?>> handlers);
}
