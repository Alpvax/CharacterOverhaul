package alpvax.mc.characteroverhaul.command;

import alpvax.mc.characteroverhaul.character.attribute.AttributeDisplayUtil;
import alpvax.mc.characteroverhaul.character.attribute.SortedModifierMap;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.StringTextComponent;

public class AttributeCommand {
  public AttributeCommand(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal("attributes")
        .then(Commands.argument("target", EntityArgument.entity())
            //.suggests()
            .executes(ctx -> execute(ctx.getSource(), EntityArgument.getEntity(ctx, "target"), false))
            .then(Commands.argument("groupBySource", BoolArgumentType.bool())
                .executes(ctx -> execute(ctx.getSource(), EntityArgument.getEntity(ctx, "target"), BoolArgumentType.getBool(ctx, "groupBySource")))
            )
        )
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
