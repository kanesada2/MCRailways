package com.github.kanesada2.MCRailways;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class MCRailwaysListener implements Listener {
	private MCRailways plugin;

	public MCRailwaysListener(MCRailways plugin) {
        this.plugin = plugin;
    }
	@EventHandler(priority = EventPriority.LOW)
	public void onMinecartMove(VehicleMoveEvent e){
		if(!(e.getVehicle() instanceof Minecart)){
			return;
		}
		Minecart cart = (Minecart)e.getVehicle();
		if(cart.hasMetadata("length")){
			double length = cart.getMetadata("length").get(0).asDouble();
			Vector velocity = e.getTo().subtract(e.getFrom()).toVector().normalize().multiply(length);
			switch(cart.getLocation().getBlock().getType()){
	    		case RAIL:
	    			cart.setVelocity(velocity);
	    			break;
	    		case ACTIVATOR_RAIL:
	    			cart.setVelocity(velocity);
	    			break;
	    		case DETECTOR_RAIL:
	    			cart.setVelocity(velocity);
	    			break;
	    		default:
	    			break;
			}
		}
		Sign sign = Util.getMySign(cart);
		if(sign == null) return;
		int checkerType = Util.getCheckerType(sign);
		if(checkerType != 0){
			InventoryHolder holder = null;
			if(cart instanceof RideableMinecart){
				if(!cart.getPassengers().isEmpty()){
					if(cart.getPassengers().get(0) instanceof Player){
						holder = (InventoryHolder)cart.getPassengers().get(0);
					}
				}
			}
			if(cart instanceof StorageMinecart || cart instanceof HopperMinecart){
				holder = (InventoryHolder)cart;
			}
			if(holder == null){
				return;
			}
			Chest chest = (Chest)sign.getLocation().add(0, -1 ,0).getBlock().getState();
			ItemStack[] inChest = chest.getInventory().getContents();
			inventoryLoop:
			for(ItemStack item: inChest){
				switch(checkerType){
					case 2:
						if(holder.getInventory().contains(item)){
							return;
						}
						break;
					case 3:
						if(holder.getInventory().contains(item)){
							new CheckerResetTask(sign.getLocation(), sign.getLines(), sign.getType()).runTaskLater(plugin, 20);
							sign.getLocation().getBlock().setType(Material.REDSTONE_BLOCK);
							break inventoryLoop;
						}
						break;
					default:
						if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
							for(ItemStack ticket: holder.getInventory().getContents()){
								if(ticket != null &&ticket.hasItemMeta() && ticket.getItemMeta().hasDisplayName()){
									if(ticket.getItemMeta().getDisplayName().contains(item.getItemMeta().getDisplayName())){
										new CheckerResetTask(sign.getLocation(), sign.getLines(), sign.getType()).runTaskLater(plugin, 20);
										sign.getLocation().getBlock().setType(Material.REDSTONE_BLOCK);
										break inventoryLoop;
									}
								}
							}
						}
						break;
				}
			}
			if(checkerType == 2){
				new CheckerResetTask(sign.getLocation(), sign.getLines(), sign.getType()).runTaskLater(plugin, 20);
				sign.getLocation().getBlock().setType(Material.REDSTONE_BLOCK);
			}
		}else if(sign.getLine(1).equalsIgnoreCase("Changer") && sign.getLine(2) != null){
			Double fixing = 0.4d;
			if(cart.hasMetadata("length")){
				cart.removeMetadata("length", plugin);
			}
			try {
				fixing = Double.parseDouble(sign.getLine(2)) / 20;
			} catch (NumberFormatException nfex) {
				return;
			}
			if(fixing == 0){
				cart.setVelocity(new Vector(0,0,0));
				return;
			}
			cart.setMetadata("length", new FixedMetadataValue(plugin, fixing));
			cart.setMaxSpeed(cart.getVelocity().normalize().multiply(fixing).length());
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onEmptySign(SignChangeEvent e){
		if(Util.isSign(e.getBlock().getType()) && e.getLine(0).isEmpty() && e.getLine(1).isEmpty() && e.getLine(3).isEmpty()){
			Block base = e.getBlock().getLocation().add(0, -1 , 0).getBlock();
			switch(base.getType()){
				case CHEST:
					if(e.getLine(2).isEmpty() || e.getLine(2).equalsIgnoreCase("Not") || e.getLine(2).equalsIgnoreCase("Item")){
						e.setLine(0, "[MCRailways]");
						e.setLine(1, "Checker");
					}
					break;
				case BLUE_GLAZED_TERRACOTTA:
					if(e.getLine(2).isEmpty()){
						e.setLine(0, "[MCRailways]");
						e.setLine(1, "Changer");
						e.setLine(2, "100");
					}
					break;
				case YELLOW_GLAZED_TERRACOTTA:
					if(e.getLine(2).isEmpty()){
						e.setLine(0, "[MCRailways]");
						e.setLine(1, "Changer");
						e.setLine(2, "20");
					}
					break;
				case RED_GLAZED_TERRACOTTA:
					if(e.getLine(2).isEmpty()){
						e.setLine(0, "[MCRailways]");
						e.setLine(1, "Changer");
						e.setLine(2, "10");
					}
					break;
				case WHITE_GLAZED_TERRACOTTA:
					if(e.getLine(2).isEmpty()){
						e.setLine(0, "[MCRailways]");
						e.setLine(1, "Changer");
					}
					break;
				default:
					break;
			}
		}
	}
}
