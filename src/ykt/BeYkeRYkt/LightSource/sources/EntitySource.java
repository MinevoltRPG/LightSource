package ykt.BeYkeRYkt.LightSource.sources;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import ykt.BeYkeRYkt.LightSource.LightAPI;
import ykt.BeYkeRYkt.LightSource.LightSource;
import ykt.BeYkeRYkt.LightSource.items.ItemManager;
import ykt.BeYkeRYkt.LightSource.items.LightItem;
import ykt.BeYkeRYkt.LightSource.nbt.comphenix.AttributeStorage;

public class EntitySource extends Source {

    public EntitySource(LivingEntity entity, Location loc, LightItem item, ItemType type, ItemStack itemStack) {
        super(entity, loc, item, type, itemStack);
    }

    @Override
    public void doTick() {
        LivingEntity entity = (LivingEntity) getOwner();
        Location newLocation = entity.getLocation();

        if (LightSource.getInstance().getDB().isEntityLight()) {
            if (LightSource.getInstance().getDB().getWorld(this.getOwner().getWorld().getName())) {
                if (!entity.isDead()) {
                    if (entity.getEquipment().getItemInHand() != null && getItemStack().equals(entity.getEquipment().getItemInHand()) && ItemManager.isLightSource(entity.getEquipment().getItemInHand())) {
                        updateLight(newLocation);
                        return;
                    } else if (entity.getEquipment().getHelmet() != null && getItemStack().equals(entity.getEquipment().getHelmet()) && ItemManager.isLightSource(entity.getEquipment().getHelmet())) {
                        updateLight(newLocation);
                        return;
                    }
                }
            }
        }
        LightAPI.deleteLight(this.getLocation(), true);
        AttributeStorage storage = AttributeStorage.newTarget(getItemStack(), ItemManager.TIME_ID);
        storage.setData(String.valueOf(getItem().getBurnTime()));
        LightAPI.getSourceManager().removeSource(this);
        return;
    }

    @Override
    protected void removeItem() {
        // remove item
        LivingEntity player = (LivingEntity) this.getOwner();
        ItemStack item = null;
        if (this.getType() == ItemType.HAND) {
            item = player.getEquipment().getItemInHand();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
                LightItem light = ItemManager.getLightItem(item);
                this.setItem(light);
                this.setItemStack(item);
            } else {
                player.getEquipment().setItemInHand(null);
            }
        } else if (this.getType() == ItemType.HELMET) {
            item = player.getEquipment().getHelmet();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
                LightItem light = ItemManager.getLightItem(item);
                this.setItem(light);
                this.setItemStack(item);
            } else {
                player.getEquipment().setHelmet(null);
            }
        }
        // End
    }
}