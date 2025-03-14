package net.detrovv.damnation.block.entity;

import net.detrovv.damnation.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities
{
    public static final BlockEntityType<CursingTableBlockEntity> CURSING_TABLE_BLOCK_ENTITY =
            register("cursing_table_block_entity", CursingTableBlockEntity::new, ModBlocks.CURSING_TABLE_BLOCK);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name,
                                                                       BlockEntityType.BlockEntityFactory<? extends T> entityFactory,
                                                                       Block... blocks)
    {
        Identifier id = Identifier.of(net.detrovv.damnation.Damnation.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, BlockEntityType.Builder.<T>create(entityFactory, blocks).build());
    }
}