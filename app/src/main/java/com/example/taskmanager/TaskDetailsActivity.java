package com.example.taskmanager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailsActivity extends AppCompatActivity {

    private Task selectedTask;

    private TextView detailsTaskTitle;
    private TextView detailsSubject;
    private TextView detailsPriority;
    private TextView detailsDueDate;
    private TextView detailsAmount;
    private TextView detailsPoints;
    private Button doneButton;
    private Button backButton;
    private Button deleteTaskButton;
    private TaskStorage taskStorage;
    private TextView statsTask;
    private TextView detailsTaskType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        detailsTaskTitle = findViewById(R.id.detailsTaskTitle);
        detailsTaskType = findViewById(R.id.detailsTaskType);
        detailsSubject = findViewById(R.id.detailsSubject);
        detailsPriority = findViewById(R.id.detailsPriority);
        detailsDueDate = findViewById(R.id.detailsDueDate);
        detailsAmount = findViewById(R.id.detailsAmount);
        detailsPoints = findViewById(R.id.detailsPoints);
        doneButton = findViewById(R.id.doneButton);
        backButton = findViewById(R.id.backButton);
        statsTask = findViewById(R.id.statsTask);
        deleteTaskButton = findViewById(R.id.deleteTaskButton);
        taskStorage = new TaskStorage(this);
        selectedTask = (Task) getIntent().getSerializableExtra("SelectedTask");

        if (selectedTask != null)
        {
            showTaskDetails();
        }


        deleteTaskButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showConfirmDialogdelete();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stats = "";
                if (selectedTask.isDone()) {
                    stats = "פתוחה";
                    statsTask.setText("סטטוס :" + stats);
                    selectedTask.setDone(false);
                    doneButton.setText("סמן משימה כבוצעה");

                } else {
                    stats = "בוצעה";
                    statsTask.setText("סטטוס :" + stats);
                    selectedTask.setDone(true);
                    doneButton.setText("ביטול סימון כבוצע");
                }

                taskStorage.updateTask(selectedTask);
                setResult(RESULT_OK);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    public void showTaskDetails()
    {
        String stats = "";
        if (selectedTask.isDone()){
            stats = "בוצעה";
        }
        else{
            stats = "פתוחה";
        }

        if (selectedTask instanceof HomeworkTask) {

            HomeworkTask homeworkTask = (HomeworkTask) selectedTask;

            detailsTaskType.setText("סוג המשימה :" + homeworkTask.getTypeName());

        } else if (selectedTask instanceof ExamTask) {

            ExamTask examTask = (ExamTask) selectedTask;

            detailsTaskType.setText("סוג המשימה :" + examTask.getTypeName());
        }

        detailsTaskTitle.setText("כותרת: " + selectedTask.getTitle());
        statsTask.setText("סטטוס :" + stats);
        detailsSubject.setText("מקצוע: " + selectedTask.getSubject());
        detailsPriority.setText("עדיפות: " + selectedTask.getPriority());
        detailsDueDate.setText("תאריך: " + selectedTask.getDueDate());

        if (selectedTask instanceof HomeworkTask) {

            HomeworkTask homeworkTask = (HomeworkTask) selectedTask;

            detailsAmount.setText("מספר תרגילים: " + homeworkTask.getExercises());

        } else if (selectedTask instanceof ExamTask) {

            ExamTask examTask = (ExamTask) selectedTask;

            detailsAmount.setText("מספר נושאים: " + examTask.getTopics());
        }

        detailsPoints.setText("נקודות: " + selectedTask.getPoints());

        if (selectedTask.isDone())
        {
            doneButton.setText("ביטול סימון");
        }
        else
        {
            doneButton.setText("סמן משימה כבוצעה");
        }
    }

    public void showConfirmDialogdelete()
    {

        new AlertDialog.Builder(this)
                .setTitle("מחיקת משימה")
                .setMessage("האם ברצונך למחוק את המשימה?")
                .setPositiveButton("מחק", (dialog, which) ->
                {

                    taskStorage.deleteById(selectedTask.getId());

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("DeletedTask", selectedTask.getId());

                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton("ביטול", null)
                .show();
    }
}