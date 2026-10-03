package com.example.taskmanager;
import android.content.DialogInterface;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    private TextView headLine;
    private TextView tvWelcome;
    private TextView studentName;
    private EditText enteredName;
    private Button enterButton;
    private Button resetButton;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);

        headLine = findViewById(R.id.headLine);
        tvWelcome = findViewById(R.id.tvWelcome);
        studentName = findViewById(R.id.studentName);
        enteredName = findViewById(R.id.enteredName);
        enterButton = findViewById(R.id.enterButton);
        resetButton = findViewById(R.id.resetButton);

        sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("name", "");

        if (!savedName.isEmpty()) {
            tvWelcome.setText("ברוך שובך,  " + savedName);
            enteredName.setText(savedName);

        } else {
            tvWelcome.setText("ברוכים הבאים");
        }

        enterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String name = enteredName.getText().toString();

                if (name.isEmpty()) {
                    enteredName.setError("יש להכניס שם");
                    Toast.makeText(MainActivity.this, "יש להכניס שם פרטי", Toast.LENGTH_SHORT).show();
                    return;
                }
                sharedPreferences.edit().putString("name", name).apply();
                studentName.setText("");
                Intent intent = new Intent(MainActivity.this, TasksActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialogres();
            }
        });
    }
    public void showConfirmDialogres() {
        new AlertDialog.Builder(this)
                .setTitle("איפוס")
                .setMessage("האם אתה בטוח שברצונך לאפס את נתוניך?")
                .setPositiveButton("כן", (d, w) -> {
                    enteredName.setText("");
                    tvWelcome.setText("ברוכים הבאים");
                    sharedPreferences.edit().remove("name").apply();

                    TaskStorage taskStorage = new TaskStorage(MainActivity.this);
                    taskStorage.deleteAllTasks();

                })
                .setNegativeButton("לא", (d, w) ->
                        Toast.makeText(this, "האיפוס התבטל", Toast.LENGTH_SHORT).show())
                .show();
    }
}