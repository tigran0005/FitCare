package com.example.fitcarehub;

public class WorkoutActivity {
    public static void main(String[] args) {
        Workout armsWorkout = new Workout("Arms");
        armsWorkout.addWorkoutItem(new WorkoutItem("url_arm_curls", "Arm Curls", 30));
        armsWorkout.addWorkoutItem(new WorkoutItem("url_push_ups", "Push Ups", 45));
        armsWorkout.addWorkoutItem(new WorkoutItem("url_tricep_dips", "Tricep Dips", 40));

        Workout legsWorkout = new Workout("Legs");
        legsWorkout.addWorkoutItem(new WorkoutItem("url_squats", "Squats", 50));
        legsWorkout.addWorkoutItem(new WorkoutItem("url_lunges", "Lunges", 60));
        legsWorkout.addWorkoutItem(new WorkoutItem("url_leg_press", "Leg Press", 70));

        printWorkoutDetails(armsWorkout);
        printWorkoutDetails(legsWorkout);
    }

    private static void printWorkoutDetails(Workout workout) {
        System.out.println("Workout Category: " + workout.getCategory());
        for (WorkoutItem item : workout.getItems()) {
            System.out.println("Name: " + item.getName() + ", Duration: " + item.getDuration() + "s, GIF: " + item.getGif());
        }
        System.out.println();
    }
}
