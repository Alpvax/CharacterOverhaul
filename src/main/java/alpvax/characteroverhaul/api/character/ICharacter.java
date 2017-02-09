package alpvax.characteroverhaul.api.character;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.effect.Effect;
import alpvax.characteroverhaul.api.effect.IEffectProvider;
import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.api.skill.Skill;
import alpvax.characteroverhaul.api.skill.SkillInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Classes that wish to implement this should extend {@link CharacterBase}.
 */
public interface ICharacter extends INBTSerializable<NBTTagCompound>
{
	@CapabilityInject(ICharacter.class)
	public static Capability<ICharacter> CAPABILITY = null;

	/**
	 * Gets the object this Character represents.
	 * @return
	 */
	public <T extends ICapabilityProvider> T getAttachedObject();

	/**
	 * Gets the world this character is in.
	 * @return
	 */
	public World getWorld();

	/**
	 * Utility method for the character's position.
	 * @return
	 */
	public Vec3d getPosition();

	/**
	 * Utility method for the direction the character is facing.<br>
	 * Should return {@link Vec3d#ZERO} if the character doesn't have an orientation (e.g. most TileEntities).
	 * @return
	 */
	public @Nonnull Vec3d getDirection();

	/**
	 * @param perk the perk to retrieve the level of.
	 * @return the level of the perk, or 0 if it isn't acquired.
	 */
	public int getPerkLevel(Perk perk);

	public void aquirePerk(Perk perk);

	public void aquirePerk(Perk perk, int level);

	//public void setPerkLevel(Perk perk, int level);

	/**
	 * @param skill the skill to retrieve the instance of.
	 * @return
	 */
	public SkillInstance getSkillInstance(Skill skill);

	/**
	 * @param skill the skill to retrieve the instance of.
	 * @return the level of the skill (ignoring experience at the current level)
	 */
	public int getSkillLevel(Skill skill);

	public void addSkillExperience(Skill skill, float amount);

	//public <T extends ICharacterModifierHandler<?>> T getModifierHandler(ResourceLocation registryName);

	//public List<ICharacterModifierHandler<?>> getModifierHandlers();

	public List<Effect> getEffects();

	public void addEffects(IEffectProvider provider);

	public void removeEffect(UUID id);

	public List<Ability> getHotbarAbilities();

	public List<Ability> getAllAbilities();

	/**
	 * Use to trigger the ability in the specified hotbar slot manually. Used by commands
	 * @param slot
	 */
	public void triggerAbilityKeybind(int slot);

	//public void addAbilities(IAbilityProvider provider);

	public void removeAbility(UUID id);

	/**
	 * Used to copy data from this to a new Character upon player respawn.
	 * @param newCharacter
	 */
	public void cloneFrom(ICharacter newCharacter);
}
