package alpvax.characteroverhaul.character;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CharacterLiving extends CharacterBase
{
	protected final EntityLivingBase entity;

	public CharacterLiving(EntityLivingBase living)
	{
		entity = living;
	}

	public EntityLivingBase getLiving()
	{
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ICapabilityProvider> T getAttachedObject()
	{
		return (T)entity;
	}

	@Override
	public AbstractAttributeMap getAttributeMap()
	{
		return entity.getAttributeMap();
	}

}
