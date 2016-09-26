package alpvax.characteroverhaul.api.character.modifier;

import java.util.List;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.skill.SkillInstance.SkillExpModifier;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class RegistryCharModHandler<T extends ICharacterModifier & IForgeRegistryEntry<T>> implements ICharacterModifierHandler<T>
{
	private final ICharacter character;
	private T modifier = null;

	public RegistryCharModHandler(ICharacter attached)
	{
		character = attached;
	}

	@Override
	public NBTTagString serializeNBT()
	{
		return new NBTTagString(modifier.getRegistryName().toString());
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		modifier = getRegistry().getValue(new ResourceLocation(((NBTTagString)nbt).getString()));
	}


	protected abstract IForgeRegistry<T> getRegistry();

	@Override
	public ICharacter getCharacter()
	{
		return character;
	}

	@Override
	public boolean setModifier(T modifier)
	{
		this.modifier = modifier;
		return true;
	}

	@Override
	public List<PerkModifier> getPerkModifiers()
	{
		return modifier.getPerkModifiers();
	}

	@Override
	public List<SkillExpModifier> getSkillModifiers()
	{
		return modifier.getSkillModifiers();
	}
}
