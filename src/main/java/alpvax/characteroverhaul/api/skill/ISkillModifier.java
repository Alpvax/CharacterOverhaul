package alpvax.characteroverhaul.api.skill;

import alpvax.characteroverhaul.api.character.modifier.ICharacterModifier;

public interface ISkillModifier extends ICharacterModifier
{

	public class SkillExpModifier implements ISkillModifier
	{

		private int operation;
		private float amount;

		public SkillExpModifier(int operation, float amount)
		{
			this.operation = operation;
			this.amount = amount;
		}

		@Override
		public float getExperienceModifier()
		{
			return amount;
		}

		@Override
		public int getOperatorType()
		{
			return operation;
		}

	}

	float getExperienceModifier();

	int getOperatorType();

}
