package com.everest.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.LightStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraveEntity extends Entity {
    private PlayerEntity owner;
    private UUID ownerUuid;
    private String ownerName;
    private final List<Pair<Integer, ItemStack>> storage = new ArrayList<>();
    private boolean collected = false;

    // * methods to set up the entity for registration
    public GraveEntity(EntityType<GraveEntity> entityType, World world) {
        super(entityType, world);
    }

    // * storing items
    public void storeItems(List<Pair<Integer, ItemStack>> items) {
        storage.addAll(items);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient && !collected) {
            if (ownerUuid != null) {
                PlayerEntity owner = this.getWorld().getPlayerByUuid(ownerUuid);
                if (owner != null && this.getBoundingBox().intersects(owner.getBoundingBox()) && owner.isAlive()) {
                    restoreItems(owner);
                    this.discard();
                }
            }
        }
    }

    private void restoreItems(PlayerEntity owner) {
        if (collected) return;
        collected = true;
        System.out.println("no items saved?");
        for (Pair<Integer, ItemStack> entry : storage) {
            int slot = entry.getFirst();
            ItemStack stack = entry.getSecond();

            if (slot >= 0 && slot < owner.getInventory().size()) {
                System.out.println("passed slot >= 0 && slot < owner.getInventory().size()");
                ItemStack current = owner.getInventory().getStack(slot);
                if (current.isEmpty()) {
                    System.out.println("setting the stack");
                    owner.getInventory().setStack(slot, stack.copy());
                } else {
                    System.out.println("spawning entity");
                    owner.getWorld().spawnEntity(new ItemEntity(owner.getWorld(), owner.getX(), owner.getY(), owner.getZ(), stack.copy()));
                }
            } else {
                System.out.println("did not pass slot >= 0 && slot < owner.getInventory().size()");
                owner.getWorld().spawnEntity(new ItemEntity(owner.getWorld(), owner.getX(), owner.getY(), owner.getZ(), stack.copy()));
            }
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
//        if (!this.getWorld().isClient && !collected) {
//            for (Pair<Integer, ItemStack> entry : storage) {
//                this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), entry.getRight().copy()));
//            }
//        }
    }

    // * getters and setters
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

    // * saving data
    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        ownerUuid = nbt.getUuid("OwnerUUID");
        NbtList items = nbt.getList("StoredItems", NbtElement.COMPOUND_TYPE);

        storage.clear();
        for (int i = 0; i < items.size(); i++) {
            NbtCompound compound = items.getCompound(i);
            int slot = compound.getInt("Slot");
            ItemStack stack = ItemStack.fromNbt(compound.getCompound("Item"));
            storage.add(Pair.of(slot, stack));
        }

        System.out.println("Loaded " + storage.size() + " items from NBT");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putUuid("OwnerUUID", ownerUuid);
        NbtList items = new NbtList();

        for (Pair<Integer, ItemStack> entry : storage) {
            NbtCompound compound = new NbtCompound();
            compound.putInt("Slot", entry.getFirst());
            compound.put("Item", entry.getSecond().writeNbt(new NbtCompound()));
            items.add(compound);
        }
        nbt.put("StoredItems", items);

        System.out.println("Saved " + storage.size() + " items to NBT");
    }

    // * methods that manage environmental interaction
    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        // Empty method to stop this entity from colliding with others
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
