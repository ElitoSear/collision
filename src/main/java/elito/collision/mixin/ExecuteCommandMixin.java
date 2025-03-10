package elito.collision.mixin;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
                        CommandManager.literal("collision")
                                .then(
                                        ExecuteCommand.addConditionLogic(
                                                root,
                                                CommandManager.argument("pos", Vec3ArgumentType.vec3()),
                                                positive,
                                                commandContext -> isColliding(commandContext.getSource().getWorld(), Vec3ArgumentType.getVec3(commandContext, "pos"))
                                        )
                                )
                );
    }

    @Unique
    private static boolean isColliding(ServerWorld world, Vec3d vec3d) {
        double d = 0.01;

        BlockPos blockPos = new BlockPos((int) vec3d.getX(), (int) vec3d.getY(), (int) vec3d.getZ());
        BlockState blockState = world.getBlockState(blockPos);


        Box box = Box.of(vec3d, d, d, d);
        return !blockState.isAir()
                && VoxelShapes.matchesAnywhere(
                blockState.getCollisionShape(world, blockPos).offset(vec3d.getX(), vec3d.getY(), vec3d.getZ()), VoxelShapes.cuboid(box), BooleanBiFunction.AND
        );
    }
}
