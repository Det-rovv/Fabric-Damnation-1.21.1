package net.detrovv.damnation.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BlockFaceRelativePositionConverter
{
    private final BlockPos POS;
    private final Direction FACING;
    private final Vec3d LOCAL_X_DIRECTION;

    public BlockFaceRelativePositionConverter(BlockPos pos, Direction facing)
    {
        POS = pos;
        FACING = facing;
        LOCAL_X_DIRECTION = getFaceLocalXDirection(FACING);
    }

    public Vec3d getGlobalPositionOfLocalPointOnFace(double distanceFromCenter, double localX, double localY)
    {
        Vec3d faceBottomLeftPos = getFaceBottomLeftPos(distanceFromCenter);
        return getGlobalPositionOfLocalPointOnFace(faceBottomLeftPos, localX, localY);
    }

    public Vec3d getGlobalPositionOfLocalPointOnFace(Vec3d origin, double localX, double localY)
    {
        return moveAlongLocalX(LOCAL_X_DIRECTION, origin, localX).add(0, localY ,0);
    }

    private Vec3d getFaceBottomLeftPos(double distanceFromCenter)
    {
        Vec3d blockCenter = POS.toCenterPos();
        Vec3i facingDirection = FACING.getVector();

        Vec3d faceCenter = blockCenter.add(
                facingDirection.getX() * distanceFromCenter,
                0,
                facingDirection.getZ() * distanceFromCenter);

        Vec3d faceBottomLeft =
                moveAlongLocalX(LOCAL_X_DIRECTION, faceCenter, -0.5)
                        .subtract(0, 0.5, 0);

        return faceBottomLeft;
    }

    private Vec3d moveAlongLocalX(Vec3d localXDirection, Vec3d position, double distance)
    {
        return position.add(
                localXDirection.getX() * distance,
                0,
                localXDirection.getZ() * distance);
    }

    private Vec3d getFaceLocalXDirection(Direction facing)
    {
        switch (facing)
        {
            case Direction.EAST: return new Vec3d(0, 0, -1);
            case Direction.WEST: return new Vec3d(0, 0, 1);
            case Direction.NORTH: return new Vec3d(-1, 0, 0);
            case Direction.SOUTH: return new Vec3d(1, 0, 0);
        }
        throw new IllegalArgumentException("Not valid horizontal facing direction");
    }
}