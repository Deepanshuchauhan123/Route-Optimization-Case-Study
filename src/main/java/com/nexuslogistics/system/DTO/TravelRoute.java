package com.nexuslogistics.system.DTO;

public class TravelRoute {

    double travelCost;

    int[] travelPath;

    public TravelRoute(double travelCost, int[] travelPath) {
        this.travelCost = travelCost;
        this.travelPath = travelPath;
    }

    public int[] getTravelPath() {
        return travelPath;
    }

    public double getTravelCost() {
        return travelCost;
    }
}
