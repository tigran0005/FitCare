package com.example.fitcarehub;

import java.util.ArrayList;
import java.util.List;

class WorkoutItem {
    private String gif;
    private String name;
    private int duration;

    public WorkoutItem(String gif, String name, int duration) {
        this.gif = gif;
        this.name = name;
        this.duration = duration;
    }

    public String getGif() { return gif; }
    public void setGif(String gif) { this.gif = gif; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
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

    public List<WorkoutItem> getItems() { return items; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

}
