package alpvax.mc.characteroverhaul.command;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import alpvax.mc.characteroverhaul.util.attribute.AttributeDisplayUtil;
import alpvax.mc.characteroverhaul.util.attribute.SortedModifierMap;
import com.google.common.collect.Sets;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Set;

public class AttributeCommand {
  static LiteralArgumentBuilder<CommandSource> buildNode() {
    return Commands.literal("attributes")
    .executes(ctx -> execute(ctx))
    .then(Commands.argument("target", EntityArgument.entity())
      .requires(source -> source.hasPermissionLevel(1))
      .executes(ctx -> execute(ctx, "target")/*ForEntity(ctx.getSource(), EntityArgument.getEntity(ctx, "target"), false)*/)
      .then(Commands.argument("groupBySource", BoolArgumentType.bool())
        .executes(ctx -> execute(ctx, "target", "groupBySource")/*ForEntity(ctx.getSource(), EntityArgument.getEntity(ctx, "target"), BoolArgumentType.getBool(ctx, "groupBySource"))*/)
      )
    );
  }

  private static int execute(CommandContext<CommandSource> ctx, String... passedArgs) throws CommandSyntaxException {
    Set<String> args = Sets.newHashSet(passedArgs);
    return executeForEntity(
        ctx.getSource(),
        args.contains("target") ? EntityArgument.getEntity(ctx, "target") : ctx.getSource().asPlayer(),
        args.contains("groupBySource") && BoolArgumentType.getBool(ctx, "groupBySource")
    );
  }

  private static int executeForEntity(CommandSource source, Entity target, boolean groupBySource) {
    if (!(target instanceof LivingEntity)) {
      return 0;
    }
    LivingEntity living = (LivingEntity)target;
    SortedModifierMap modifierMap = new SortedModifierMap(living);
    if (modifierMap.isEmpty()) {
      source.sendFeedback(new TranslationTextComponent(CharacterOverhaul.MODID + ".attributeinfo.none"), false);
    }
    if (!groupBySource) {
      modifierMap.groupByAttribute().asMap().forEach((key, val) -> {
        source.sendFeedback(AttributeDisplayUtil.getAttributeSummary(living.getAttribute(key)), false);
        val.forEach(text ->
            source.sendFeedback(new StringTextComponent("  ").appendSibling(text), false));
      });
    } else {
      modifierMap.groupBySource().asMap().forEach((key, val) -> {
        source.sendFeedback(key.getName(living), false);
        val.forEach(text ->
            source.sendFeedback(new StringTextComponent("  ").appendSibling(text), false));
      });
    }
    return 1;
  }
}
