package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private TextView headlineTitle;
    private Spinner taskType;
    private TextView textTitle;
    private EditText title;

    private TextView textSubject;
    private Spinner subject;

    private TextView textPriority;
    private Spinner priority;

    private TextView textDueDate;
    private EditText dueDate;

    private TextView textAmount;
    private EditText amount;

    private Button saveButton;
    private Button cancelButton;

    private TaskStorage taskStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        headlineTitle = findViewById(R.id.headlineTitle);
        taskType = findViewById(R.id.taskType);
        textTitle = findViewById(R.id.textTitle);
        title = findViewById(R.id.title);

        textSubject = findViewById(R.id.textSubject);
        subject = findViewById(R.id.subject);

        textPriority = findViewById(R.id.textPriority);
        priority = findViewById(R.id.priority);

        textDueDate = findViewById(R.id.textDueDate);
        dueDate = findViewById(R.id.dueDate);

        textAmount = findViewById(R.id.textAmount);
        amount = findViewById(R.id.amount);

        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        taskStorage = new TaskStorage(this);
        taskType.setSelection(1);

        taskType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                if (position == 1) {
                    textAmount.setText("מספר תרגילים:");
                    textDueDate.setText("תאריך הגשה");
                    title.setHint("הכנס כותרת, למשל : עמוד 30");
                } else {
                    textAmount.setText("מספר נושאים:");
                    textDueDate.setText("תאריך בחינה");
                    title.setHint("הכנס כותרת, למשל : מבחן בביולוגיה");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String titleText = title.getText().toString();
                String dueDateText = dueDate.getText().toString();
                String amountText = amount.getText().toString();


                if (titleText.isEmpty()) {
                    title.setError("יש להכניס כותרת למשימה");
                    Toast.makeText(AddTaskActivity.this, "יש להכניס כותרת למשימה", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (dueDateText.isEmpty())
                {
                        dueDate.setError("יש להכניס תאריך חוקי");
                        Toast.makeText(AddTaskActivity.this, "תאריך לא תקין", Toast.LENGTH_SHORT).show();
                        return;
                }
                if (dueDateText.length() != 5 || dueDateText.charAt(2) != '/')
                {

                    dueDate.setError("יש להכניס תאריך חוקי, למשל 12/12");

                    Toast.makeText(AddTaskActivity.this, "תאריך לא תקין", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (amountText.isEmpty()) {
                    if (taskType.getSelectedItemPosition() == 0) {
                        Toast.makeText(AddTaskActivity.this, "יש להכניס מספר נושאים למבחן", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddTaskActivity.this, "יש להכניס מספר תרגילים לשיעורי בית", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                int amountNumber = Integer.parseInt(amountText);

                if (amountNumber <= 0) {
                    amount.setError("יש להכניס מספר שלם וגדול מ-0");
                    Toast.makeText(AddTaskActivity.this, "יש להכניס מספר שלם וגדול מ-0", Toast.LENGTH_SHORT).show();
                    return;
                }

                int id = taskStorage.nextId();
                Intent resultIntent = new Intent();
                if (taskType.getSelectedItem().toString().equals("שיעורי בית"))
                {
                    HomeworkTask newTask = new HomeworkTask(id, titleText, subject.getSelectedItem().toString(),
                            priority.getSelectedItem().toString(), dueDateText, false, amountNumber);

                    resultIntent.putExtra("New_HomeworkTask", newTask);

                } else
                {
                    ExamTask newTask = new ExamTask(id, titleText, subject.getSelectedItem().toString(),
                            priority.getSelectedItem().toString(), dueDateText, false, amountNumber);

                    resultIntent.putExtra("New_ExamTask", newTask);
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

    }
}