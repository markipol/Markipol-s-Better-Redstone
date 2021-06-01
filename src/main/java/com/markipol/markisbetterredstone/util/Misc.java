package com.markipol.markisbetterredstone.util;

import org.apache.logging.log4j.LogManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

public class Misc {
	public static void send(PlayerEntity player, String message) {
		player.sendMessage(new StringTextComponent(message), Util.NIL_UUID);
	}

	public static void log(String string) {
		LogManager.getLogger().info(string);
	}
	
	public static void warn(String string) {
		LogManager.getLogger().warn(string);
	}

	public static float scale2scale(float x, float x1, float x2, float y1, float y2) {
		if (x1 > x2) {
			float tempX = x1;
			x1 = x2;
			x2 = tempX;
			float tempY = y1;
			y1 = y2;
			y2 = tempY;
		}
		if (x <= x1)
			return y1;
		if (x >= x2)
			return y2;
		float xFraction = (x - x1) / (x2 - x1);
		return y1 + xFraction * (y2 - y1);
	}

}
