package alpvax.characteroverhaul.api.character;

import alpvax.characteroverhaul.api.perk.Perk;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
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

	/**
	 * Utility method to provide a shortcut to the AttributeMap.<br>
	 * May return null if Attributes aren't supported on this object;
	 * @return
	 */
	public AbstractAttributeMap getAttributeMap();

	public void removeModifier(CharacterModifier modifier);

	public void applyModifier(CharacterModifier modifier);

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
