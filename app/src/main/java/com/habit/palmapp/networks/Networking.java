package com.habit.palmapp.networks;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.habit.palmapp.models.Habit;


import java.util.ArrayList;
import java.util.List;

public class Networking {

    private DatabaseReference databaseReference;

    public Networking() {
        databaseReference = FirebaseDatabase.getInstance().getReference(); // Initialize the database reference
    }

    // Define an interface for callbacks
    public interface HabitsCallback {
        void onHabitsFetched(List<Habit> habits);
        void onError(String errorMessage);
    }

    // Method to get habits from Firebase
    public void getHabitsForToday(String todayDate, final HabitsCallback callback) {
        //databaseReference.child("userhabits").child(todayDate).addValueEventListener(new ValueEventListener() {
        databaseReference.child("userhabits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Habit> habitsList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Habit habit = ds.getValue(Habit.class);
                    habitsList.add(habit);
                }
                callback.onHabitsFetched(habitsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    // Method to update habit status
    public void updateHabitStatus(String todayDate, String habitId, boolean status) {
        //databaseReference.child("userhabits").child(todayDate).child(habitId).child("status").setValue(status);
        databaseReference.child("userhabits").child(habitId).child("status").setValue(status);
    }

    // Method to remove a habit
    public void removeHabit(String todayDate, String habitId) {
        //databaseReference.child("userhabits").child(todayDate).child(habitId).removeValue();
        databaseReference.child("userhabits").child(habitId).removeValue();
    }


    // Method to add a new habit
    public void addHabit(String todayDate, String habitName, final AddHabitCallback callback) {
        String habitId = databaseReference.child("userhabits").push().getKey(); // Generate unique ID
        Habit habit = new Habit(habitName, habitId, false); // Create the habit object

        // Save the habit in the database
        //databaseReference.child("userhabits").child(todayDate).child(habitId).setValue(habit)
        databaseReference.child("userhabits").child(habitId).setValue(habit)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess("New habit added");
                        } else {
                            callback.onError("Failed to add habit");
                        }
                    }
                });
    }

    // Callback interface for adding a habit
    public interface AddHabitCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
}
