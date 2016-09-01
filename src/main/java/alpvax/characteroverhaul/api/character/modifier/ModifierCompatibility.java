package alpvax.characteroverhaul.api.character.modifier;

import java.util.List;

public abstract class ModifierCompatibility
{
	public static interface Incompatibility
	{
		public List<ICharacterModifier> getRelevantModifiers();

		public String getRequirementsString();
	}

	public abstract List<Incompatibility> testCompatibility();
}
