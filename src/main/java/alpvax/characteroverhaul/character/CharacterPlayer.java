package alpvax.characteroverhaul.character;

import net.minecraft.entity.player.EntityPlayer;

public class CharacterPlayer extends CharacterLiving
{
	public CharacterPlayer(EntityPlayer player)
	{
		super(player);
	}

	public EntityPlayer getPlayer()
	{
		return this.<EntityPlayer>getAttachedObject();
	}

}
