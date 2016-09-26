package alpvax.characteroverhaul.api.character.modifier;

import java.util.List;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.skill.SkillInstance.SkillExpModifier;

public interface ICharacterModifier
{
	public boolean isValidForCharacter(ICharacter character);

	public void onAttach(ICharacter character);

	public void onDetach(ICharacter character);

	public List<PerkModifier> getPerkModifiers();

	public List<SkillExpModifier> getSkillModifiers();
}
