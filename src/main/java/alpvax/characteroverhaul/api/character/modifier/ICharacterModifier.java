package alpvax.characteroverhaul.api.character.modifier;

public interface ICharacterModifier
{
	public static interface IPerkModifier extends ICharacterModifier
	{
		/**
		 * @return the difference in maximum level
		 */
		public int getMaxLevelModifier();

		/**
		 * Uses same operator type as {@linkplain net.minecraft.entity.ai.attributes.AttributeModifier
		 * AttributeModifier}, with the addition of 3 which is a flat increase.
		 * @return 0 for a flat increase to the base value.<br>
		 *         1 for a flat base multiplier.<br>
		 *         2 for a stacking modifier.<br>
		 *         3 for a flat increase.
		 */
		public int getOperatorType();
	}

	public static interface ISkillModifier extends ICharacterModifier
	{
		/**
		 * @return the modifier applied each time experience is gained.
		 */
		public float getExperienceModifier();

		/**
		 * Uses same operator type as {@linkplain net.minecraft.entity.ai.attributes.AttributeModifier
		 * AttributeModifier}, with the addition of 3 which is a flat increase.
		 * @return 0 for a flat increase to the base value.<br>
		 *         1 for a base multiplier increase.<br>
		 *         2 for a stacking modifier.<br>
		 *         3 for a flat increase.
		 */
		public int getOperatorType();
	}

	public static final class SkillExpModifier implements ISkillModifier
	{
		private int operation;
		public float amount;

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
}
