package alpvax.characteroverhaul.api.character;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import alpvax.characteroverhaul.api.effect.ICharacterEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IAffected
{
	@CapabilityInject(IAffected.class)
	public static Capability<IAffected> CAPABILITY = null;

	public List<ICharacterEffect> getEffects();

	public void addEffect(ICharacterEffect effect);

	public void removeEffect(UUID id);

	/**
	 * Gets the object this Character represents.
	 * @return
	 */
	public <T extends ICapabilityProvider> T getAttachedObject();

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
}
