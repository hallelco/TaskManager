package com.example.taskmanager;
import android.widget.AdapterView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private TextView myTasks;
    private TextView tvHello;
    private TextView tasksCount;
    private TextView doneTasks;
    private TextView pointsCount;
    private Spinner subjectsFilter;
    private ListView tasksList;
    private Button addTaskButton;
    private Button prevScreenButton;

    private TaskStorage taskStorage;
    private ArrayList<Task> tasks;
    private ArrayAdapter<Task> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);


        myTasks = findViewById(R.id.myTasks);
        tvHello = findViewById(R.id.tvHello);
        tasksCount = findViewById(R.id.tasksCount);
        doneTasks = findViewById(R.id.doneTasks);
        pointsCount = findViewById(R.id.pointsCount);
        subjectsFilter = findViewById(R.id.subjectsFilter);
        tasksList = findViewById(R.id.tasksList);
        addTaskButton = findViewById(R.id.addTaskButton);
        prevScreenButton = findViewById(R.id.prevScreenButton);
        taskStorage = new TaskStorage(this);


        String name = getIntent().getStringExtra("name");
        tvHello.setText("שלום, " + name );


        tasks = taskStorage.loadAllTasks();
        adapter = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, new ArrayList<>(tasks));
        tasksList.setAdapter(adapter);

        subjectsFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedSubject = subjectsFilter.getSelectedItem().toString();

                ArrayList<Task> filteredTasks = new ArrayList<>();

                if (selectedSubject.equals("הכל")) {
                    filteredTasks.addAll(tasks);
                } else {
                    for (Task task : tasks) {
                        if (task.getSubject().equals(selectedSubject)) {
                            filteredTasks.add(task);
                        }
                    }
                }

                adapter.clear();
                adapter.addAll(filteredTasks);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int position, long id) {

                Task selectedTask = (Task) list.getItemAtPosition(position);
                Intent intent = new Intent(TasksActivity.this, TaskDetailsActivity.class);
                intent.putExtra("SelectedTask", selectedTask);
                startActivityForResult(intent, 2);            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TasksActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        prevScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialogprev();
            }
        });
    }

    private void updateStats() {

        int completed = 0;
        int points = 0;

        for (Task task : tasks) {

            if (task.isDone()) {
                completed++;
                points += task.getPoints();
            }
        }

        tasksCount.setText(" משימות: " + tasks.size());
        doneTasks.setText("משימות שהושלמו: " + completed);
        pointsCount.setText("נקודות: " + points);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Task task = null;

            if (data.hasExtra("New_HomeworkTask"))
            {
                task = (HomeworkTask) data.getSerializableExtra("New_HomeworkTask");
            }

            if (data.hasExtra("New_ExamTask"))
            {
                task = (ExamTask) data.getSerializableExtra("New_ExamTask");
            }

            if (task != null)
            {
                taskStorage.addTask(task);
            }
            tasks.clear();
            tasks.addAll(taskStorage.loadAllTasks());
            adapter.clear();
            adapter.addAll(tasks);
            adapter.notifyDataSetChanged();
            updateStats();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {

            tasks.clear();
            tasks.addAll(taskStorage.loadAllTasks());
            adapter.clear();
            adapter.addAll(tasks);
            adapter.notifyDataSetChanged();

            updateStats();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        tasks.clear();
        tasks.addAll(taskStorage.loadAllTasks());

        if (adapter != null)
        {
            adapter.clear();
            adapter.addAll(tasks);
            adapter.notifyDataSetChanged();
        }

        updateStats();
    }
    public void showConfirmDialogprev() {
        new AlertDialog.Builder(this)
                .setTitle("יציאה")
                .setMessage("האם ברצונך לצאת למסך הכניסה ?")
                .setPositiveButton("כן", (dialog, which) -> {
                    Intent intent = new Intent(TasksActivity.this, MainActivity.class);
                    startActivity(intent);})
                .setNegativeButton("לא", (d, w) ->
                        Toast.makeText(this, "נשארת במסך הבית", Toast.LENGTH_SHORT).show())
                .show();
    }
}