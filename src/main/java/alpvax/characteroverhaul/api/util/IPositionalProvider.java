package alpvax.characteroverhaul.api.util;

import javax.annotation.Nonnull;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IPositionalProvider
{
	/**
	 * Gets the object this represents.
	 * @return
	 */
	public <T extends ICapabilityProvider> T getAttachedObject();

	/**
	 * Gets the world this is in.
	 * @return
	 */
	public World getWorld();

	/**
	 * Utility method for the affectable's position.
	 * @return
	 */
	public Vec3d getPosition();

	/**
	 * Utility method for the direction the character is facing.<br>
	 * Should return {@link Vec3d#ZERO} if the character doesn't have an orientation (e.g. most TileEntities).
	 * @return
	 */
	public @Nonnull Vec3d getDirection();
}
