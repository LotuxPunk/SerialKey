package fr.skyost.serialkey.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.skyost.serialkey.SerialKeyAPI;

public class BunchOfKeysListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private final void onPlayerInteract(final PlayerInteractEvent event) {
		switch(event.getAction()) {
		case RIGHT_CLICK_BLOCK:
			if(SerialKeyAPI.hasPadlock(event.getClickedBlock().getLocation())) {
				break;
			}
		case RIGHT_CLICK_AIR:
			SerialKeyAPI.createInventory(event.getItem(), event.getPlayer());
		default:
			break;
		}
	}
	
	@EventHandler
	private final void onInventoryClick(final InventoryClickEvent event) {
		if(!SerialKeyAPI.isBunchOfKeys(event.getInventory())) {
			return;
		}
		final ItemStack item = event.getCurrentItem();
		if(item.getType() != Material.AIR && !SerialKeyAPI.isUsedKey(item)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	private final void onInventoryClose(final InventoryCloseEvent event) {
		final Inventory inventory = event.getInventory();
		if(!SerialKeyAPI.isBunchOfKeys(inventory)) {
			return;
		}
		final ItemStack bunchOfKeys = event.getPlayer().getInventory().getItemInMainHand();
		SerialKeyAPI.clearKeys(bunchOfKeys);
		for(final ItemStack item : inventory.all(SerialKeyAPI.getConfig().keyMaterial).values()) {
			SerialKeyAPI.addKey(bunchOfKeys, item);
		}
	}

}
