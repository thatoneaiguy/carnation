package com.everest.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.TextColor;
import net.minecraft.world.World;
//? if <1.19.0 {
import net.minecraft.util.Formatting;
/*import net.minecraft.util.Formatting;*/
//?}

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraveEntity extends Entity {
    private PlayerEntity owner;
    private UUID ownerUuid;
    private String ownerName;
    private final List<Pair<Integer, ItemStack>> storage = new ArrayList<>();
    private boolean collected = false;
    private int storedLevel;
    private int storedPoints;
    private TextColor graveColor = TextColor.parse("white");

    public GraveEntity(EntityType<GraveEntity> entityType, World world) {
        super(entityType, world);
    }

    public void storeItems(List<Pair<Integer, ItemStack>> items) {
        storage.addAll(items);
    }

    public void storeXP(int level, int points, int percent) {
        level = level * (percent / 100);
        this.storedLevel = level;
        this.storedPoints = points;
    }

    @Override
    public void tick() {
        super.tick();
        //? if >=1.20.0 {
        if (!this.getWorld().isClient && !collected) {
            if (ownerUuid != null) {
                PlayerEntity owner = this.getWorld().getPlayerByUuid(ownerUuid);
                if (owner != null && this.getBoundingBox().intersects(owner.getBoundingBox()) && owner.isAlive()) {
                    restoreItems(owner);
                    this.discard();
                }
            }
        }
        //?} else {
        /*if (!this.getWorld().isClient && !collected) {
            if (ownerUuid != null) {
                PlayerEntity owner = this.getWorld().getPlayerByUuid(ownerUuid);
                if (owner != null && this.getBoundingBox().intersects(owner.getBoundingBox()) && owner.isAlive()) {
                    restoreItems(owner);
                    this.discard();
                }
            }
        }*/
        //?}
    }

    private void restoreItems(PlayerEntity owner) {
        if (collected) return;
        collected = true;

        for (Pair<Integer, ItemStack> entry : storage) {
            int slot = entry.getFirst();
            ItemStack stack = entry.getSecond();

            if (slot >= 0 && slot < owner.getInventory().size()) {
                ItemStack current = owner.getInventory().getStack(slot);
                if (current.isEmpty()) {
                    owner.getInventory().setStack(slot, stack.copy());
                } else {
                    //? if >=1.20.0 {
                    owner.getWorld().spawnEntity(new ItemEntity(owner.getWorld(), owner.getX(), owner.getY(), owner.getZ(), stack.copy()));
                    //?} else {
                    /*owner.getWorld().spawn(new ItemEntity(owner.getWorld(), owner.getX(), owner.getY(), owner.getZ(), stack.copy()));*/
                    //?}
                }
            } else {
                //? if >=1.20.0 {
                owner.getWorld().spawnEntity(new ItemEntity(owner.getWorld(), owner.getX(), owner.getY(), owner.getZ(), stack.copy()));
                //?} else {
                /*owner.getWorld().spawn(new ItemEntity(owner.getWorld(), owner.getX(), owner.getY(), owner.getZ(), stack.copy()));*/
                //?}
            }
        }

        //? if >=1.21.0 {
        /*owner.giveExperienceLevels(storedLevel);
        owner.giveExperiencePoints(storedPoints);*/
        //?} else {
        owner.addExperienceLevels(storedLevel);
        owner.addExperience(storedPoints);
        //?}

        storedLevel = 0;
        storedPoints = 0;
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
    }

    public void setOwner(PlayerEntity owner) {
        this.ownerUuid = owner.getUuid();
        this.ownerName = owner.getDisplayName().toString();
        this.setCustomName(owner.getDisplayName());
        this.setCustomNameVisible(true);
        this.setGlowing(true);
        this.owner = owner;
    }

    public PlayerEntity getOwner() {
        return this.owner;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        //? if >=1.19.0 {
        ownerUuid = nbt.getUuid("OwnerUUID");
        //?} else {
        /*ownerUuid = UUID.fromString(nbt.getString("OwnerUUID"));*/
        //?}

        NbtList items = nbt.getList("StoredItems", NbtElement.COMPOUND_TYPE);
        storage.clear();
        for (int i = 0; i < items.size(); i++) {
            NbtCompound compound = items.getCompound(i);
            int slot = compound.getInt("Slot");
            ItemStack stack = ItemStack.fromNbt(compound.getCompound("Item"));
            storage.add(Pair.of(slot, stack));
        }

        //? if >=1.19.0 {
        if (nbt.contains("GraveColor")) {
            graveColor = TextColor.parse(nbt.getString("GraveColor"));
        }
        //?} else {
        /*if (nbt.contains("GraveColor")) {
            graveColor = Formatting.valueOf(nbt.getString("GraveColor").toUpperCase(Locale.ROOT));
        }*/
        //?}

        storedLevel = nbt.getInt("StoredLevel");
        storedPoints = nbt.getInt("StoredPoints");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        //? if >=1.19.0 {
        nbt.putUuid("OwnerUUID", ownerUuid);
        //?} else {
        /*nbt.putString("OwnerUUID", ownerUuid.toString());*/
        //?}

        NbtList items = new NbtList();
        for (Pair<Integer, ItemStack> entry : storage) {
            NbtCompound compound = new NbtCompound();
            compound.putInt("Slot", entry.getFirst());
            compound.put("Item", entry.getSecond().writeNbt(new NbtCompound()));
            items.add(compound);
        }
        nbt.put("StoredItems", items);

        //? if >=1.19.0 {
        nbt.putString("GraveColor", graveColor.toString());
        //?} else {
        /*nbt.putString("GraveColor", graveColor.getName());*/
        //?}

        nbt.putInt("StoredLevel", storedLevel);
        nbt.putInt("StoredPoints", storedPoints);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        // No-op to prevent pushing
    }

    @Override
    public boolean collidesWith(Entity other) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }
}
