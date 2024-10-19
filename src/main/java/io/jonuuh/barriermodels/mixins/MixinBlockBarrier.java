package io.jonuuh.barriermodels.mixins;

import io.jonuuh.barriermodels.config.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = BlockBarrier.class)
public abstract class MixinBlockBarrier extends Block
{
    @Unique
    private static final Map<EnumFacing, PropertyBool> barrierModels$CONNECTION_PROPERTIES = barrierModels$initPropertyMap();

    // BlockState properties are used to connect barrier models (determine which block model to use per barrier block)
    // 64 combinations (unique models) given 6 sides that could contain an adjacent barrier
    // (64 = sum combinations of 6 choosing from 0 to 2, multiplied by two (0=6, 1=5, etc.), plus combination of 6 choosing 3)
    @Unique
    private static Map<EnumFacing, PropertyBool> barrierModels$initPropertyMap()
    {
        Map<EnumFacing, PropertyBool> map = new HashMap<>();
        map.put(EnumFacing.NORTH, PropertyBool.create("north"));
        map.put(EnumFacing.EAST, PropertyBool.create("east"));
        map.put(EnumFacing.SOUTH, PropertyBool.create("south"));
        map.put(EnumFacing.WEST, PropertyBool.create("west"));
        map.put(EnumFacing.UP, PropertyBool.create("up"));
        map.put(EnumFacing.DOWN, PropertyBool.create("down"));
        return map;
    }

    // Does nothing (I think), just to make the compiler happy (- extending 'Block')
    private MixinBlockBarrier(Material materialIn)
    {
        super(materialIn);
    }

    // Makes barriers able to be rendered at all, default barrier renderType = -1: skipped in BlockRendererDispatcher -> renderBlock()
    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    private void barrierModels$modifyRenderType(CallbackInfoReturnable<Integer> cir)
    {
        if (Settings.getInstance().doRendering())
        {
            cir.setReturnValue(super.getRenderType()); // default Block renderType = 3
        }
    }

    // Makes barriers render in the same layer as translucent blocks (like stained-glass)
    @Override
    public EnumWorldBlockLayer getBlockLayer()
    {
        return Settings.getInstance().doRendering() ? EnumWorldBlockLayer.TRANSLUCENT : super.getBlockLayer();
    }

    // Makes barrier sides not render through other barriers (copied from BlockBreakable -> glass superclass)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        if (!Settings.getInstance().doRendering())
        {
            return super.shouldSideBeRendered(worldIn, pos, side);
        }

        IBlockState state = worldIn.getBlockState(pos);

        if (worldIn.getBlockState(pos.offset(side.getOpposite())) != state)
        {
            return true;
        }
        if (state.getBlock() == this)
        {
            return false;
        }

        return super.shouldSideBeRendered(worldIn, pos, side);
    }

    // Determines what barrier model will be rendered. BlockRendererDispatcher class uses this method to map a block's state to its model
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (!Settings.getInstance().doConnecting())
        {
            return super.getActualState(state, worldIn, pos); // all properties false
        }

        for (EnumFacing enumFacing : EnumFacing.VALUES)
        {
            state = state.withProperty(barrierModels$CONNECTION_PROPERTIES.get(enumFacing), worldIn.getBlockState(pos.offset(enumFacing)).getBlock() == Blocks.barrier);
        }
        return state;
    }

    // Create a BlockState with the connection properties (used in block registry init?)
    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, barrierModels$CONNECTION_PROPERTIES.values().toArray(new IProperty[0]));
    }

    // TODO: Not really positive why/if this is necessary, found it in an example for creating custom block properties
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }
}
