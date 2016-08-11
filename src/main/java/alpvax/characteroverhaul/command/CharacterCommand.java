package alpvax.characteroverhaul.command;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.perk.Perk;
import alpvax.characteroverhaul.capabilities.CapabilityCharacterHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CharacterCommand extends CommandBase
{
	private static enum CharCmd
	{
		TEST
		{
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs)
			{
				if(target == null)
				{
					sender.addChatMessage(new TextComponentTranslation("commands.character.error.notarget"));
				}
				else
				{
					sender.addChatMessage(new TextComponentString(target.toString()));//TODO: improve test output before release
				}
			}
		},
		PERKS
		{
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs)
			{
				if(target == null)
				{
					target = getCharacter(sender);
				}
				ITextComponent perks = new TextComponentString(":");
				int num = 0;
				for(Perk perk : Perk.REGISTRY.getValues())
				{
					int level;
					if((level = target.getPerkLevel(perk)) != 0)
					{
						num++;
						ITextComponent text = new TextComponentTranslation(perk.getLocalisedName() + ", ");
						text.setStyle(new Style().setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponentString(Integer.toString(level)))));
						perks.appendSibling(text);
					}
				}
				ITextComponent message = new TextComponentTranslation("character.perks.num", target, num);
				if(num > 0)
				{
					message.appendSibling(perks);
				}
				sender.addChatMessage(message);
			}
		},
		SKILLS
		{
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs)
			{
				if(target==null){target=getCharacter(sender);}
				// TODO Auto-generated method stub

			}
		}/*,
			MODIFIERS
			{
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs)
			{
				if(target == null)
				{
					target = getCharacter(sender);
				}
				// TODO Auto-generated method stub
			
			}
			},
			ABILITIES
			{
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs)
			{
				if(target == null)
				{
					target = getCharacter(sender);
				}
				// TODO Auto-generated method stub
			
			}
			},
			EFFECTS
			{
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs)
			{
				if(target == null)
				{
					target = getCharacter(sender);
				}
				// TODO Auto-generated method stub
			
			}
			}*/;

		private boolean match(String arg)
		{
			return name().toLowerCase().startsWith(arg.toLowerCase());
		}

		public abstract void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs);
	}

	private static final CharCmd[] CMD_PARTS = CharCmd.values();

	@Override
	public String getCommandName()
	{
		return "character";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.character.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1)
		{
			throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
		}
		ICharacter target = null;
		List<String> cmdArgs = Lists.newArrayList(args);
		Iterator<String> i = cmdArgs.listIterator();
		int argsIndex = -1;
		boolean playerSpecified = !(sender instanceof ICapabilityProvider) || !((ICapabilityProvider)sender).hasCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null);
		while(i.hasNext())
		{
			String arg = i.next();
			argsIndex++;
			//Handle finding target
			if(isUsernamePrefix(arg))
			{
				Entity es;
				if(argsIndex == args.length - 1 && (es = sender.getCommandSenderEntity()) != null)
				{//If target prefix is the last part of the command, use whatever is being looked at.

					Vec3d look = es.getLookVec();
					if(look != null)
					{
						Vec3d start = new Vec3d(es.posX, es.posY + (double)es.getEyeHeight(), es.posZ);
						Vec3d end = start.add(look.scale(256));//Maximum distance to search
						RayTraceResult hit = es.worldObj.rayTraceBlocks(start, end);
						if(hit.typeOfHit == Type.ENTITY && hit.entityHit != null)
						{
							target = hit.entityHit.getCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null);
						}
						else if(hit.typeOfHit == Type.BLOCK)
						{
							TileEntity tile = es.worldObj.getTileEntity(hit.getBlockPos());
							if(tile != null)
							{
								target = tile.getCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, hit.sideHit);
							}
						}
					}
				}
				i.remove();
				continue;
			}
			if(isUsernameIndex(args, argsIndex))
			{
				playerSpecified = true;
				if(target != null)
				{
					sender.addChatMessage(new TextComponentTranslation("commands.character.error.multipletargets", target.<EntityPlayer> getAttachedObject().getDisplayNameString(), arg));
				}
				else
				{
					try
					{
						target = getCharacter(server, sender, arg);
					}
					catch(PlayerNotFoundException e)
					{
						sender.addChatMessage(new TextComponentTranslation("commands.generic.player.notFound"));
					}
				}
				i.remove();
				continue;
			}
		}
		if(target == null && playerSpecified)
		{
			throw new PlayerNotFoundException();
		}
		for(CharCmd cmd : CMD_PARTS)
		{
			if(cmd.match(cmdArgs.get(0)))
			{
				cmdArgs.remove(0);//Remove command itself
				cmd.execute(server, sender, target, cmdArgs);
			}
		}
	}

	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if(args.length == 1)
		{
			/**
			 * Returns a List of strings (chosen from the given strings) which the last word in the given string array
			 * is a beginning-match for. (Tab completion).
			 */
			return getListOfStringsMatchingLastWord(args, new String[]{"give", "take"});
		}
		else if(args.length != 2)
		{
			return args.length == 3 ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.<String> emptyList();
		}
		else
		{
			List<String> list = Lists.<String> newArrayList();

			for(StatBase statbase : AchievementList.ACHIEVEMENTS)
			{
				list.add(statbase.statId);
			}

			return getListOfStringsMatchingLastWord(args, list);
		}
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	public boolean isUsernameIndex(String[] args, int index)
	{
		return isUsernamePrefix(args[index - 1]);
	}

	private boolean isUsernamePrefix(String arg)
	{
		return arg.matches("(?i:[-/@]p(layer)?)");
	}

	private static ICharacter getCharacter(ICommandSender sender)
	{
		return ((ICapabilityProvider)sender).getCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null);
	}
	
	public static ICharacter getCharacter(MinecraftServer server, ICommandSender sender, String target) throws PlayerNotFoundException
	{
		return getPlayer(server, sender, target).getCapability(CapabilityCharacterHandler.CHARACTER_CAPABILITY, null);
	}
}