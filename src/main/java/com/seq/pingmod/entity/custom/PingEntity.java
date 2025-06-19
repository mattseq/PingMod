package com.seq.pingmod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

public class PingEntity extends Entity {
    int LIFETIME = 10*20;
    int liveTick = 0;

    public PingEntity(EntityType<? extends PingEntity> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        liveTick++;
        if (liveTick >= LIFETIME) {
            this.discard();
        }
        super.tick();
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {}

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(1.0F, 1.0F);
    }

}
