package com.habit.palmapp.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habit.palmapp.R;
import com.habit.palmapp.adapters.HabitsAdapter;
import com.habit.palmapp.models.Habit;

import java.util.ArrayList;

public class HabitsActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean isActive = true ;
    private  int habitsize = 0 ;

    LinearLayoutManager layoutManager ;

    HabitsAdapter habitsAdapter ;
    ArrayList<Habit> habitslist = new ArrayList<>();

    RecyclerView habitsrv ;
    //used to store habit by date , for daily updates
    String todayDate = "" ;
    EditText habitnameet ;
    ImageButton addbtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);
        initlaizevariables();
        setlisteners();
        gethabits();
    }


    private void gethabits() {
        //get habits list from database and update the list using habits adapter
    }

    private void initlaizevariables() {

        addbtn = (ImageButton)findViewById(R.id.addbtn);
        habitnameet = (EditText)findViewById(R.id.habitet);
        habitsrv = (RecyclerView) findViewById(R.id.habitsrv);




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