package com.habit.palmapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.habit.palmapp.models.Habit;
import com.habit.palmapp.repo.HabitRepository;

import java.util.List;

public class HabitViewModel extends ViewModel {

    private HabitRepository habitRepository;
    private LiveData<List<Habit>> habitListLiveData;

    public HabitViewModel() {
        habitRepository = new HabitRepository();
        habitListLiveData = habitRepository.getHabitList();
    }

    public LiveData<List<Habit>> getHabitList() {
        return habitListLiveData;
    }
}
