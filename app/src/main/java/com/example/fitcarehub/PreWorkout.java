package com.example.fitcarehub;

public class PreWorkout {
    String exerciseName;
    int time;
    int gif;

    public PreWorkout(String exerciseName, int time, int gif) {
        this.exerciseName = exerciseName;
        this.time = time;
        this.gif = gif;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getGif() {
        return gif;
    }

    public void setGif(int gif) {
        this.gif = gif;
    }

    public String getTimeFormatted() {
        int minutes = time / 60;
        int remainingSeconds = time % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}
