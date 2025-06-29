package com.energy.percentageservice.model;

import java.util.List;

public class PercentageMessage {
    private List<Integer> hour;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public List<Integer> getHour() {
        return hour;
    }

    public void setHour(List<Integer> hour) {
        this.hour = hour;
    }

    public double getCommunityProduced() {
        return communityProduced;
    }

    public void setCommunityProduced(double communityProduced) {
        this.communityProduced = communityProduced;
    }

    public double getCommunityUsed() {
        return communityUsed;
    }

    public void setCommunityUsed(double communityUsed) {
        this.communityUsed = communityUsed;
    }

    public double getGridUsed() {
        return gridUsed;
    }

    public void setGridUsed(double gridUsed) {
        this.gridUsed = gridUsed;
    }

    public PercentageMessage() {
        // Needed for Jackson
    }
}
