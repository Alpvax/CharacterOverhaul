package alpvax.characteroverhaul.api.character;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import alpvax.characteroverhaul.api.ability.Ability;
import alpvax.characteroverhaul.api.effect.Effect;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public int getPerkLevel(@Nonnull Perk perk);

	public void aquirePerk(@Nonnull Perk perk);

	public void aquirePerk(@Nonnull Perk perk, int level);

	//public void setPerkLevel(Perk perk, int level);

	/**
	 * @param skill the skill to retrieve the instance of.
	 * @return
	 */
	public SkillInstance getSkillInstance(@Nonnull Skill skill);

	/**
	 * @param skill the skill to retrieve the instance of.
	 * @return the level of the skill (ignoring experience at the current level)
	 */
	public int getSkillLevel(@Nonnull Skill skill);

	public void addSkillExperience(@Nonnull Skill skill, float amount);

	//public <T extends ICharacterModifierHandler<?>> T getModifierHandler(ResourceLocation registryName);

	//public List<ICharacterModifierHandler<?>> getModifierHandlers();

	public Effect getEffect(@Nonnull UUID effectID);

	public List<Effect> getEffects();

	public void addEffect(@Nonnull Effect effect);

	public void removeEffect(@Nonnull UUID id);

	public Ability[] getHotbarAbilities();

	public List<Ability> getAllAbilities();

	/**
	 * Use to trigger the ability in the specified hotbar slot manually. Used by commands and keybinds.
	 * @param slot
	 */
	public void triggerAbility(int slot);

	public void addAbility(@Nonnull Ability ability);

	public void removeAbility(@Nonnull UUID id);

	public void setHotbarSlot(int i, UUID id);//TODO:Clean up, implement properly and document

	@SideOnly(Side.CLIENT)
	public void updateAbilityClient(@Nonnull UUID id, @Nullable Ability ability);

	/**
	 * Used to copy data from this to a new Character upon player respawn.
	 * @param newCharacter
	 */
	public void cloneFrom(@Nonnull ICharacter newCharacter);
}
