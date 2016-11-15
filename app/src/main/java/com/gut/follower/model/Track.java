package com.gut.follower.model;

import java.util.List;


public class Track {

    private String id;

    private String accountId;

    private String activity;

    private boolean finished;

    private Double avgSpeed;

    private Double runPace;

    private Double distance;

    private long startTime;

    private long finishTime;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    private List<GutLocation> locations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public Double getRunPace() {
        return runPace;
    }

    public void setRunPace(Double runPace) {
        this.runPace = runPace;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<GutLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<GutLocation> locations) {
        this.locations = locations;
    }
}
