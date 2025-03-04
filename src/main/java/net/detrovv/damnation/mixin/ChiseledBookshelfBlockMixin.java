package net.detrovv.damnation.mixin;

import net.detrovv.damnation.Damnation;
import net.detrovv.damnation.util.BlockFaceRelativePositionConverter;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(ChiseledBookshelfBlock.class)
public abstract class ChiseledBookshelfBlockMixin extends BlockWithEntity
{
    private static final Random RANDOM = new Random();
    private static final double BOOK_WIDTH = 4.0/16;
    private static final double SHORT_BOOK_HEIGHT = 5.0/16;
    private static final double TALL_BOOK_HEIGHT = 6.0/16;
    private static final double PARTICLE_SPAWN_PROBABILITY = 0.005;

    protected ChiseledBookshelfBlockMixin(Settings settings)
    {
        super(settings);
    }

    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
    {
        return !world.isClient ? validateTicker(type, BlockEntityType.CHISELED_BOOKSHELF, ChiseledBookshelfBlockMixin::tick) : null;
    }

    @Unique
    private static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity)
    {
        if (blockEntity instanceof ChiseledBookshelfBlockEntity bookshelfEntity)
        {
            Damnation.LOGGER.info("isClient: " + world.isClient);
            for (int i = 0; i < bookshelfEntity.size(); i++)
            {
                ItemStack itemStack = bookshelfEntity.getStack(i);

                if (itemStack.getItem() == Items.ENCHANTED_BOOK &&
                        RANDOM.nextFloat() < PARTICLE_SPAWN_PROBABILITY &&
                        containsCurses(itemStack))
                {
                    spawnCurseParticle(pos, state, i, (ServerWorld) world);
                }
            }
        }
    }

    private static boolean containsCurses(ItemStack itemStack)
    {
        for (var enchantmentEntry : EnchantmentHelper.getEnchantments(itemStack).getEnchantments())
        {
            if (enchantmentEntry.isIn(EnchantmentTags.CURSE)) return true;
        }
        return false;
    }

    private static void spawnCurseParticle(BlockPos pos, BlockState state, int bookIndex, ServerWorld serverWorld)
    {
        Vec3d particleSpawnPos = getRandomPositionOnBook(pos, state, bookIndex);

        serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                particleSpawnPos.x,
                particleSpawnPos.y,
                particleSpawnPos.z,
                1, 0,0,0 ,0);
    }

    private static Vec3d getRandomPositionOnBook(BlockPos pos, BlockState state, int bookIndex)
    {
        var converter = new BlockFaceRelativePositionConverter(pos, state.get(HorizontalFacingBlock.FACING));

        var bookLocalLeftBottom = getBookLeftBottomCorner(bookIndex);

        Vec3d bookLeftBottomPos = converter.getGlobalPositionOfLocalPointOnFace(
                0.55,
                bookLocalLeftBottom.x + 0.5/16,
                bookLocalLeftBottom.y + 0.5/16);

        Vec3d bookRightTopPos = converter.getGlobalPositionOfLocalPointOnFace(
                bookLeftBottomPos,
                BOOK_WIDTH - 0.5/16,
                getBookHeightBySlot(bookIndex) - 0.5/16);

        return getRandomVectorBetween(bookLeftBottomPos, bookRightTopPos, RANDOM);
    }

    private static Vector2d getBookLeftBottomCorner(int slotIndex)
    {
        switch (slotIndex)
        {
            case 0: return new Vector2d(1.0/16, 9.0/16);
            case 1: return new Vector2d(6.0/16, 9.0/16);
            case 2: return new Vector2d(11.0/16, 9.0/16);
            case 3: return new Vector2d(1.0/16, 1.0/16);
            case 4: return new Vector2d(6.0/16, 1.0/16);
            case 5: return new Vector2d(11.0/16, 1.0/16);
        }
        throw new IllegalArgumentException("Not valid chiseled bookshelf slot");
    }

    private static double getBookHeightBySlot(int slotIndex)
    {
        switch (slotIndex)
        {
            case 0, 4: return SHORT_BOOK_HEIGHT;
            default: return TALL_BOOK_HEIGHT;
        }
    }

    private static Vec3d getRandomVectorBetween(Vec3d vec1, Vec3d vec2, Random random)
    {
        double x = vec1.x + (vec2.x - vec1.x) * random.nextDouble();
        double y = vec1.y + (vec2.y - vec1.y) * random.nextDouble();
        double z = vec1.z + (vec2.z - vec1.z) * random.nextDouble();

        return new Vec3d(x, y, z);
    }
}