package com.nexuslogistics.system.DTO;

import java.util.Objects;

public class Location {

    private int stopId;
    private int coordinateX;
    private int coordinateY;

    public int getStopId() {
        return stopId;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return stopId == location.stopId && coordinateX == location.coordinateX && coordinateY == location.coordinateY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopId, coordinateX, coordinateY);
    }
}
