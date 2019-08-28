package com.github.kanesada2.MCRailways;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckerResetTask extends BukkitRunnable {
	private String[] lines;
	private Location signLoc;
	private Material signMaterial;
	public CheckerResetTask(Location signLoc, String[] lines, Material signMaterial){
		this.signLoc = signLoc;
		this.lines = lines;
		this.signMaterial = signMaterial;
	}
	@Override
	public void run() {
		signLoc.getBlock().setType(signMaterial);
		Sign sign = (Sign)signLoc.getBlock().getState();
		for(int i = 0; i < lines.length; i++){
			sign.setLine(i, lines[i]);
		}
		sign.update(true);

	}

}
