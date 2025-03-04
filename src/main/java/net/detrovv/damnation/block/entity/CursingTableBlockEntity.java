package net.detrovv.damnation.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CursingTableBlockEntity extends BlockEntity
{
    public CursingTableBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CURSING_TABLE_BLOCK_ENTITY, pos, state);
    }
}
