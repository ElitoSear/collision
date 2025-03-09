package elito.collision.mixin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Position;
import net.minecraft.server.command.ExecuteCommand.add

@Mixin(ExecuteCommand.class)
public class ExecuteCommandMixin {
    @Inject(at = @At("HEAD"), method = "addConditionArguments")
    private static void addConditionArguments(
            CommandNode<ServerCommandSource> root,
            LiteralArgumentBuilder<ServerCommandSource> argumentBuilder,
            boolean positive, CommandRegistryAccess commandRegistryAccess,
            CallbackInfoReturnable<ArgumentBuilder<ServerCommandSource, ?>> cir) {

        argumentBuilder
                .then(
                        CommandManager.literal("loaded")
                                .then(
                                        addConditionLogic(
                                                root,
                                                CommandManager.argument("pos", BlockPosArgumentType.blockPos()),
                                                positive,
                                                commandContext -> isColliding(commandContext.getSource().getWorld(), BlockPosArgumentType.getBlockPos(commandContext, "pos"))
                                        )
                                )
                );
    }

    @Unique
    private static void isColliding(ServerWorld world, Position position) {

    }
}
