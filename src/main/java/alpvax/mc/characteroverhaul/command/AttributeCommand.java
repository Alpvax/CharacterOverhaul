package alpvax.mc.characteroverhaul.command;

import alpvax.mc.characteroverhaul.character.attribute.AttributeDisplayUtil;
import alpvax.mc.characteroverhaul.character.attribute.SortedModifierMap;
import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.Set;

public class AttributeCommand {
  public AttributeCommand(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal("attributes")
        .executes(ctx -> execute(ctx))
        .then(Commands.argument("target", EntityArgument.entity())
            .requires(source -> source.hasPermissionLevel(1))
            .executes(ctx -> execute(ctx.getSource(), EntityArgument.getEntity(ctx, "target"), false))
            .then(Commands.argument("groupBySource", BoolArgumentType.bool())
                .executes(ctx -> execute(ctx.getSource(), EntityArgument.getEntity(ctx, "target"), BoolArgumentType.getBool(ctx, "groupBySource")))
            )
        )
    );
  }

  private static int execute(CommandContext<CommandSource> ctx, String... passedArgs) throws CommandSyntaxException {
    Set<String> args = Sets.newHashSet(passedArgs);
    return execute(
        ctx.getSource(),
        args.contains("target") ? EntityArgument.getEntity(ctx, "target") : ctx.getSource().asPlayer(),
        args.contains("groupBySource") ? BoolArgumentType.getBool(ctx, "groupBySource") : false
    );
  }

  private static int execute(CommandSource source, Entity target, boolean groupBySource) {
    if (!(target instanceof LivingEntity)) {
      return 0;
    }
    LivingEntity living = (LivingEntity)target;
    SortedModifierMap modifierMap = new SortedModifierMap(living);
    if (!groupBySource) {
      modifierMap.groupByAttribute().asMap().entrySet().forEach(e -> {
        source.sendFeedback(AttributeDisplayUtil.getAttributeSummary(living.getAttribute(e.getKey())), false);
        e.getValue().forEach(text ->
            source.sendFeedback(new StringTextComponent("  ").appendSibling(text), false));
      });
    } else {
      modifierMap.groupBySource().asMap().entrySet().forEach(e -> {
        source.sendFeedback(e.getKey().getName(living), false);
        e.getValue().forEach(text ->
            source.sendFeedback(new StringTextComponent("  ").appendSibling(text), false));
      });
    }
    return 1;
  }
}
