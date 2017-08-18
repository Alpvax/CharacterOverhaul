package alpvax.characteroverhaul.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;

import alpvax.characteroverhaul.api.CharacterOverhaul;
import alpvax.characteroverhaul.api.character.ICharacter;
import alpvax.characteroverhaul.api.perk.Perk;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
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
		HELP
		{
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs)
			{
				sender.sendMessage(new TextComponentTranslation("commands.character.usage", CMD_NAMES.get().collect(Collectors.joining(",\n\t"))));
				//TODO: better help
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, List<String> args)
			{
				return Collections.emptyList();
			}

		},
		TEST
		{
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs)
			{
				if(target == null)
				{
					sender.sendMessage(new TextComponentTranslation("commands.character.error.notarget"));
				}
				else
				{
					sender.sendMessage(new TextComponentString(target.toString()));//TODO: improve test output before release
				}
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, List<String> args)
			{
				// TODO Auto-generated method stub
				return null;
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
				sender.sendMessage(message);
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, List<String> args)
			{
				// TODO Auto-generated method stub
				return null;
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

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, List<String> args)
			{
				// TODO Auto-generated method stub
				return null;
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

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, List<String> args)
			{
				// TODO Auto-generated method stub
				return null;
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

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, List<String> args)
			{
				// TODO Auto-generated method stub
				return null;
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

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, List<String> args)
			{
				// TODO Auto-generated method stub
				return null;
			}
			}*/;

		private boolean match(String arg)
		{
			return name().toLowerCase().startsWith(arg.toLowerCase());
		}

		private static CharCmd get(String arg)
		{
			for(CharCmd cmd : CMD_PARTS)
			{
				if(cmd.match(arg))
				{
					return cmd;
				}
			}
			return null;
		}

		public abstract void execute(MinecraftServer server, ICommandSender sender, ICharacter target, List<String> cmdArgs);

		public abstract List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, List<String> args);
	}

	private static final CharCmd[] CMD_PARTS = CharCmd.values();
	private static final Supplier<Stream<String>> CMD_NAMES = () -> Arrays.stream(CMD_PARTS).map(CharCmd::name);


	private class CommandParts
	{
		private final MinecraftServer server;
		private final ICommandSender sender;
		private final ICharacter target;
		private final CharCmd command;
		private final List<String> args;

		public CommandParts(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			this.server = server;
			this.sender = sender;
			ICharacter target = null;
			List<String> cmdArgs = Lists.newArrayList(args);
			Iterator<String> i = cmdArgs.listIterator();
			int argsIndex = -1;
			boolean playerSpecified = !(sender instanceof ICapabilityProvider) || !((ICapabilityProvider)sender).hasCapability(ICharacter.CAPABILITY, null);
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
							Vec3d start = new Vec3d(es.posX, es.posY + es.getEyeHeight(), es.posZ);
							Vec3d end = start.add(look.scale(256));//Maximum distance to search
							RayTraceResult hit = es.world.rayTraceBlocks(start, end);
							if(hit.typeOfHit == Type.ENTITY && hit.entityHit != null)
							{
								target = hit.entityHit.getCapability(ICharacter.CAPABILITY, null);
							}
							else if(hit.typeOfHit == Type.BLOCK)
							{
								TileEntity tile = es.world.getTileEntity(hit.getBlockPos());
								if(tile != null)
								{
									target = tile.getCapability(ICharacter.CAPABILITY, hit.sideHit);
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
						sender.sendMessage(new TextComponentTranslation("commands.character.error.multipletargets", target.<EntityPlayer> getAttachedObject().getDisplayNameString(), arg));
					}
					else
					{
						try
						{
							target = getCharacter(server, sender, arg);
						}
						catch(PlayerNotFoundException e)
						{
							sender.sendMessage(new TextComponentTranslation("commands.generic.player.notFound"));
						}
					}
					i.remove();
					continue;
				}
			}
			if(target == null && playerSpecified)
			{
				throw new PlayerNotFoundException("commands.generic.player.unspecified");
			}
			this.target = target;
			command = CharCmd.get(cmdArgs.get(0));
			if(command == null)
			{
				throw new CommandNotFoundException("commands.character.options", CMD_NAMES.get().collect(Collectors.joining(",\n\t")));
			}
			this.args = cmdArgs.subList(1, cmdArgs.size());
		}

		public void execute()
		{
			command.execute(server, sender, target, args);
		}

		public List<String> getTabCompletionOptions()
		{
			return command.getTabCompletionOptions(server, sender, args);
		}
	}

	@Override
	public String getName()
	{
		return "character";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.character.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1)
		{
			throw new WrongUsageException(getUsage(sender), CMD_NAMES.get().collect(Collectors.joining(",\n\t")));
		}
		CommandParts cmd = new CommandParts(server, sender, args);
		cmd.execute();
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if(args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, CMD_NAMES.get().toArray(String[]::new));
		}
		if(isUsernameIndex(args, args.length - 1))
		{
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		try
		{
			CommandParts cmd = new CommandParts(server, sender, args);
			return cmd.getTabCompletionOptions();
		}
		catch(CommandException e)
		{
			CharacterOverhaul.log(Level.ERROR, e, "Error getting tab completion options for: {}", String.join(", ", args));
		}
		return Collections.emptyList();
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index < 1 ? false : isUsernamePrefix(args[index - 1]);
	}

	private boolean isUsernamePrefix(String arg)
	{
		return arg.matches("(?i:[-/@]p(layer)?)");
	}

	private static ICharacter getCharacter(ICommandSender sender)
	{
		return ((ICapabilityProvider)sender).getCapability(ICharacter.CAPABILITY, null);
	}

	public static ICharacter getCharacter(MinecraftServer server, ICommandSender sender, String target) throws CommandException
	{
		return getPlayer(server, sender, target).getCapability(ICharacter.CAPABILITY, null);
	}
}