package alpvax.mc.characteroverhaul.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Supplier;

public class CharacterCommand {
  public static String[] commandAliases = {"character", "characteroverhaul"};
  public CharacterCommand(CommandDispatcher<CommandSource> dispatcher) {
    System.err.println("Registering Character commands");
    ArgumentBuilder[] subArgs = {
        register(AttributeCommand::buildNode, dispatcher),
        register(SkillCommand::buildNode, dispatcher)
    };
    Arrays.stream(commandAliases).map(Commands::literal).forEach(argbuilder -> {
      Arrays.stream(subArgs).forEach(argbuilder::then);
      System.err.println("Registering Literal command: " + argbuilder.getLiteral());
      dispatcher.register(argbuilder);
    });
  }

  static LiteralArgumentBuilder<CommandSource> register(Supplier<LiteralArgumentBuilder<CommandSource>> commandNode, @Nullable CommandDispatcher<CommandSource> dispatcher) {
    LiteralArgumentBuilder<CommandSource> builder = commandNode.get();
    System.err.println("Registering Character command: " + builder.getLiteral());
    if (dispatcher != null) {
      System.err.println("Registering Literal command: " + builder.getLiteral());
      dispatcher.register(builder);
    }
    return builder;
  }
}
