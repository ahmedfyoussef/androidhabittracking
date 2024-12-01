package com.habit.palmapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.habit.palmapp.R;
import com.habit.palmapp.models.Habit;


import java.util.ArrayList;
import java.util.List;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.MyViewHolder> {

    private List<Habit> habitList;
    private OnItemClickListener onItemClickListener;


    private Context context ;
    private View.OnClickListener onClickListener;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public   TextView title  ;
        public  ImageButton deleteBtn ;
        public  CheckBox checkBox ;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);


            deleteBtn = (ImageButton)view.findViewById(R.id.ib);
            checkBox = (CheckBox) view.findViewById(R.id.cb);



        }


    }
    public void setHabitList(List<Habit> habitList) {
        this.habitList = habitList;
        notifyDataSetChanged();
    }

    public HabitsAdapter(List<Habit> habits, Context context   ) {


        this.habitList = habits;
        this.context = context ;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Habit habit = habitList.get(position);
        setHabitInfo(habit,holder.title,holder.checkBox,holder.deleteBtn);
        // Set click listeners for the checkbox and delete button


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = holder.checkBox.isChecked();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(habit, R.id.cb, isChecked);
                }
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(habit,R.id.ib);
                }
            }
        });
    }

    private void setHabitInfo(Habit habit,
                              TextView title, CheckBox cb, ImageButton ib) {
        title.setText(habit.getHabit() );
        if(habit.isStatus()){
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }



    }

     public interface OnItemClickListener {
        void onItemClick(Habit habit, int viewId);
        void onItemClick(Habit habit, int viewId, boolean isChecked);
    }

    // Set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }



    @Override
    public int getItemCount() {
        return habitList.size();
    }
    public void addAll(List<Habit> newaichats) {
        int initialSize = habitList.size();

        habitList.addAll(newaichats);

        notifyItemRangeInserted(initialSize, newaichats.size());
    }

    //this functions are used on pagination for large database

    public void clearlist(){
        habitList.clear();
    }

    public String getLastItemId() {
        return habitList.get(habitList.size() - 1).getHabitid();
    }
    public String getFirstItemId() {
        return habitList.get(0).getHabitid();
    }

    public void removeLastItem(){

        habitList.remove(0);




    }




}