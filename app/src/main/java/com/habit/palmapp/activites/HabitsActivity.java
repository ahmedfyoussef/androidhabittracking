package com.habit.palmapp.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habit.palmapp.R;
import com.habit.palmapp.adapters.HabitsAdapter;
import com.habit.palmapp.models.Habit;
import com.habit.palmapp.networks.Networking;
import com.habit.palmapp.utils.Utils;
import com.habit.palmapp.viewmodels.HabitViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HabitsActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean isActive = true ;
    private  int habitsize = 0 ;

    LinearLayoutManager layoutManager ;

    HabitsAdapter habitsAdapter ;

    ArrayList<Habit> habitslist = new ArrayList<>();
    private List habits = new ArrayList<>();
    RecyclerView habitsrv ;
    //used to store habit by date , for daily updates
    String todayDate = "" ;
    EditText habitnameet ;
    ImageButton addbtn ;
    private Networking networking;
    private Utils utils ;
    private HabitViewModel habitViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);
        initlaizevariables();
        setlisteners();

        gettodaydate();


        try {

            //gethabits();
            gethabitsmvvm();
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(),"Failed to get habits from database,try again",Toast.LENGTH_SHORT).show();


        }

    }
    private void initlaizevariables() {

        addbtn = (ImageButton)findViewById(R.id.addbtn);
        habitnameet = (EditText)findViewById(R.id.habitet);
        habitsrv = (RecyclerView) findViewById(R.id.habitsrv);
        networking = new Networking();
        utils = new Utils();



    }
    private void gettodaydate(){
        //used to store habits by date to be auto daily list
        todayDate = utils.getTodayDate() ;


    }
    private void gethabitsmvvm(){
        habitsrv.setLayoutManager(new LinearLayoutManager(this));
        habitsAdapter  = new HabitsAdapter( habits,this);
        habitsrv.setAdapter(habitsAdapter);

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        habitViewModel.getHabitList().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {


                habitslist.clear();
                habitslist.addAll(habits);
                habitsAdapter.setHabitList(habits);
            }
        });
        habitsAdapter.setOnItemClickListener(new HabitsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Habit habit, int viewId) {

                if (viewId == R.id.ib) {
                    networking.removeHabit(todayDate, habit.getHabitid());

                }
            }

            @Override
            public void onItemClick(Habit habit, int viewId, boolean isChecked) {
                networking.updateHabitStatus(todayDate, habit.getHabitid(), isChecked);
                if (viewId == R.id.cb) {
                    if (isChecked) {
                        Toast.makeText(getApplicationContext(),
                                "You completed " + habit.getHabit() + " for today!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                habit.getHabit() + " not completed so far", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    private void gethabits() {
        //get habits list from database and update the list using habits adapter

            // this is for calling the networking functions in a background thread on future work we can use MVVM and live data
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // Perform the network request in the background
                    networking.getHabitsForToday(todayDate, new Networking.HabitsCallback() {
                        @Override
                        public void onHabitsFetched(List<Habit> habits) {
                            // Post the result to the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isActive) {
                                        habitslist.clear();
                                        habitslist.addAll(habits);

                                        habitsAdapter = new HabitsAdapter(habitslist, getApplicationContext());
                                        layoutManager = new LinearLayoutManager(getApplicationContext());
                                        habitsrv.setLayoutManager(layoutManager);

                                        habitsAdapter.setOnItemClickListener(new HabitsAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(Habit habit, int viewId) {
                                                if (viewId == R.id.ib) {
                                                    networking.removeHabit(todayDate, habit.getHabitid());

                                                }
                                            }

                                            @Override
                                            public void onItemClick(Habit habit, int viewId, boolean isChecked) {
                                                networking.updateHabitStatus(todayDate, habit.getHabitid(), isChecked);

                                                if (viewId == R.id.cb) {
                                                    if (isChecked) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "You completed " + habit.getHabit() + " for today!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(),
                                                                habit.getHabit() + " not completed so far", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });
                                        habitsAdapter.notifyDataSetChanged();
                                        habitsrv.setItemAnimator(new DefaultItemAnimator());
                                        habitsrv.setAdapter(habitsAdapter);

                                        if (habitsize > 0) {
                                            habitsrv.scrollToPosition(habitsize - 1);
                                        } else {
                                            habitsrv.scrollToPosition(habitsize);
                                        }

                                        habitsize = habitslist.size();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Post error message to the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }


    private void setlisteners(){
        addbtn.setOnClickListener(this);
    }

    //handle add btn click listner
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addbtn) {
            String habitName = habitnameet.getText().toString().trim();


            if (habitName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Habit name can't be empty", Toast.LENGTH_SHORT).show();
            } else {
                // create habit object and store in database



            }
        }
    }
    //handle app life cycle
    public  void setactivityinactive(){
        isActive  = false ;
    }

    public  void setactivityactive(){
        isActive  = true ;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setactivityactive();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setactivityactive();
        gethabitsmvvm();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setactivityinactive();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setactivityinactive();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setactivityinactive();
    }
}