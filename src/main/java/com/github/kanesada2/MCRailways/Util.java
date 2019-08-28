package com.github.kanesada2.MCRailways;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;

public class Util {
    public static boolean isSign(Material material){
        if(material == Material.ACACIA_SIGN) return true;
        if(material == Material.BIRCH_SIGN) return true;
        if(material == Material.DARK_OAK_SIGN) return true;
        if(material == Material.JUNGLE_SIGN) return true;
        if(material == Material.OAK_SIGN) return true;
        if(material == Material.SPRUCE_SIGN) return true;
        return false;
    }

    public static Sign getMySign(Minecart cart){
        Block block = cart.getLocation().add(0, -2 ,0).getBlock();
        if(!isSign(block.getType())) return null;
        Sign sign = (Sign)block.getState();
        if(!(sign.getLine(0).equalsIgnoreCase("[MCRailways]"))) return null;
        return sign;
    }

    public static int getCheckerType(Sign sign){
        if(!(sign.getLine(1).equalsIgnoreCase("checker") && sign.getLocation().add(0, -1 ,0).getBlock().getType() == Material.CHEST)) return 0;
        if(sign.getLine(2).equalsIgnoreCase("Not")) return 2;
        if(sign.getLine(2).equalsIgnoreCase("Not")) return 3;
        return 1;
    }
}