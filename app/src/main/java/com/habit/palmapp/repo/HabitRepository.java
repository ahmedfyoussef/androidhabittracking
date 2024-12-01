package com.habit.palmapp.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.habit.palmapp.models.Habit;


import java.util.ArrayList;
import java.util.List;

public class HabitRepository {

    private DatabaseReference databaseReference;
    private MutableLiveData<List<Habit>> habitListLiveData;

    public HabitRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("userhabits");
        habitListLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Habit>> getHabitList() {
        fetchHabitsFromFirebase();
        return habitListLiveData;
    }

    private void fetchHabitsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Habit> habitList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Habit habit = snapshot.getValue(Habit.class);
                    habitList.add(habit);
                }
                habitListLiveData.setValue(habitList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors here
            }
        });
    }
}
