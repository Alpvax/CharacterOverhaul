package alpvax.mc.characteroverhaul.command;

import alpvax.mc.characteroverhaul.CharacterOverhaul;
import alpvax.mc.characteroverhaul.character.ICharacter;
import alpvax.mc.characteroverhaul.character.capability.CharacterCapability;
import alpvax.mc.characteroverhaul.character.skill.Skill;
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

public class SkillCommand {
  static LiteralArgumentBuilder<CommandSource> buildNode() {
    return Commands.literal("skills")
    .executes(ctx -> execute(ctx))
    .then(Commands.argument("target", EntityArgument.entity())
        .requires(source -> source.hasPermissionLevel(1))
        .executes(ctx -> execute(ctx, "target"))
    );
  }

  private static int execute(CommandContext<CommandSource> ctx, String... passedArgs) throws CommandSyntaxException {
    Set<String> args = Sets.newHashSet(passedArgs);
    return executeForEntity(
        ctx.getSource(),
        args.contains("target") ? EntityArgument.getEntity(ctx, "target") : ctx.getSource().asPlayer()
    );
  }

  private static int executeForEntity(CommandSource source, Entity target) {
    if (!(target instanceof LivingEntity)) {
      return 0;
    }
    LivingEntity living = (LivingEntity)target;
    ICharacter character = target.getCapability(CharacterCapability.CHARACTER_CAPABILITY)
        .orElseThrow(() -> new NullPointerException(String.format("Entity {} does not have the character capability", target)));
    character.getSkills().forEach((skill, level, xp) -> source.sendFeedback(new TranslationTextComponent(CharacterOverhaul.MODID + ".skill.display", skill.getName(), level, xp), false));
    return 1;
  }
}
