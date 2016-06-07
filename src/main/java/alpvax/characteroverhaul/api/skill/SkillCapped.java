package alpvax.characteroverhaul.api.skill;

public abstract class SkillCapped extends Skill
{
	private final int maxLevel;

	public SkillCapped(String id, int levelCap)
	{
		super(id);
		maxLevel = levelCap;
	}

}
