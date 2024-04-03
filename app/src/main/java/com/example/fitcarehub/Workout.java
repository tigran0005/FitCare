package com.example.fitcarehub;

import java.util.ArrayList;
import java.util.List;

class WorkoutItem {
    private String gif;
    private String name;
    private int duration;
    private int count;
    private int restTime;

    public WorkoutItem(String gif, String name, int duration, int count, int restTime) {
        this.gif = gif;
        this.name = name;
        this.duration = duration;
        this.count = count;
        this.restTime = restTime;
    }


    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

public class Workout {
    private List<WorkoutItem> items;
    private String category;

    public Workout(String category) {
        this.items = new ArrayList<>();
        this.category = category;
    }

    public void addWorkoutItem(WorkoutItem item) {
        this.items.add(item);
    }

    public void addWorkoutItems(List<WorkoutItem> items) {
        this.items.addAll(items);
    }

    public List<WorkoutItem> getItems() {
        return items;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
