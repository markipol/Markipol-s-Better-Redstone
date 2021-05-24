package com.markipol.markisbetterredstone.common.blocks.junction_gate;

import net.minecraft.util.Direction;

public class DistanceToEdge implements Comparable<DistanceToEdge> {
	Direction dir;
	double dis;
	public DistanceToEdge (Direction dir, double dis) {
		this.dir = dir;
		this.dis = dis;
	}
	public double getDistance() {
		return dis;
	}
	public Direction getDirection() {
		return dir;
	}
	@Override
	public int compareTo(DistanceToEdge other) {
		
		return Double.compare(getDistance(), other.getDistance());
	}
}