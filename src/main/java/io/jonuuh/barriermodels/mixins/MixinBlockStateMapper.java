package io.jonuuh.barriermodels.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBarrier;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(BlockStateMapper.class)
public abstract class MixinBlockStateMapper
{
    @Shadow
    private Set<Block> setBuiltInBlocks;

    // Remove barriers from a set of special blocks which won't have models created for them.
    // Because of this removal, somewhere in the code (not sure EXACTLY where) barriers will have models created for them
    // during game init using the blockstates, models & texture files in assets/minecraft
    @Inject(method = "registerBuiltInBlocks", at = @At(value = "TAIL"))
    private void barrierModels$modifyBuiltInBlocksRegistry(Block[] p_178448_1_, CallbackInfo ci)
    {
        setBuiltInBlocks.removeIf(block -> block instanceof BlockBarrier);
    }
}
