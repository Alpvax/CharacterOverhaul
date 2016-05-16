package alpvax.characteroverhaul.api.character;

import java.util.UUID;

import alpvax.characteroverhaul.api.ability.AbilityInstance;
import alpvax.characteroverhaul.api.ability.IAbility;
import alpvax.characteroverhaul.api.perk.Perk;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface ICharacter
{
	/**
	 * @param perk the perk to retrieve the level of.
	 * @return the level of the perk, or 0 if it isn't acquired.
	 */
	public int getPerkLevel(Perk perk);

	public void setPerkLevel(Perk perk, int level);

	/**
	 * Gets the object this Character represents.
	 * @return
	 */
	public <T extends ICapabilityProvider> T getAttachedObject();

	public AbilityInstance getModifier(UUID id);

	public boolean hasAbility(IAbility ability);

	public void addAbility(IAbility ability);

	public void removeAbility(IAbility ability);

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
	public Vec3d getDirection();


	/**
	 * Used to copy data from this to a new Character upon player respawn.
	 * @param newCharacter
	 */
	public void cloneTo(ICharacter newCharacter);
}
