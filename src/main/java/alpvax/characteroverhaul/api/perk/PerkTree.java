package alpvax.characteroverhaul.api.perk;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import alpvax.characteroverhaul.api.character.ICharacter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public class PerkTree extends IForgeRegistryEntry.Impl<PerkTree>
{
	private final List<Perk> perks = new ArrayList<>();

	public PerkTree(String id)
	{
		this(id, DefaultPermissionLevel.ALL, "");
	}

	public PerkTree(String id, DefaultPermissionLevel permissionLevel, String permissionDescription)
	{
		Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Attempted instantiation of PerkTree \"%s\" with no id", toString());
		setRegistryName(id);
		PermissionAPI.registerNode(getPermissionKey(), permissionLevel, permissionDescription);
	}

	public boolean hasAccessToTree(ICharacter character)
	{
		ICapabilityProvider obj = character.getAttachedObject();
		if(obj instanceof EntityPlayer)
		{
			return PermissionAPI.hasPermission((EntityPlayer)obj, getPermissionKey());
		}
		//Non players are allowed access to all trees.
		return true;
	}

	public List<Perk> getDisplayerdPerksForCharacter(ICharacter character)
	{
		return Lists.newArrayList(perks);
	}


	public String getPermissionKey()
	{
		ResourceLocation r = getRegistryName();
		return "perk." + r.getResourceDomain() + r.getResourcePath();
	}

	private static IForgeRegistry<PerkTree> REGISTRY = GameRegistry.findRegistry(PerkTree.class);

	public static List<PerkTree> getAllTrees()
	{
		return REGISTRY.getValues();
	}
}
