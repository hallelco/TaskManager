package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;

public class TaskStorage
{
    private SharedPreferences preferences;
    private Gson gson;

    public TaskStorage(Context context)
    {

        preferences = context.getSharedPreferences(
                "tasks_preferences", Context.MODE_PRIVATE
        );
        gson = new Gson();
    }

    public ArrayList<Task> loadAllTasks()
    {
        String json = preferences.getString("tasks_json", null);
        ArrayList<Task> tasks = new ArrayList<>();

        if (json == null) {
            return tasks;
        }
        JsonArray taskList = JsonParser.parseString(json).getAsJsonArray();

        for (JsonElement task : taskList) {
            JsonObject obj = task.getAsJsonObject();
            String tasktype;
            if (obj.has("exercises")) {
                tasktype = "שיעורי בית";
            } else {
                tasktype = "מבחן";
            }
            if (tasktype.equals("שיעורי בית")) {
                HomeworkTask homeworkTask = gson.fromJson(obj, HomeworkTask.class);
                tasks.add(homeworkTask);

            } else if (tasktype.equals("מבחן")) {
                ExamTask examTask = gson.fromJson(obj, ExamTask.class);
                tasks.add(examTask);
            }
        }
        return tasks;
    }

    public void saveAllTasks(ArrayList<Task> tasks)
    {
        String json = gson.toJson(tasks);
        preferences.edit().putString("tasks_json", json).apply();
    }

    public void addTask(Task task)
    {
        ArrayList<Task> tasks = loadAllTasks();
        tasks.add(task);
        saveAllTasks(tasks);
    }

    public int nextId()
    {
        ArrayList<Task> tasks = loadAllTasks();
        int maxId = 0;
        for (Task task : tasks) {
            if (task.getId() > maxId)
            {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }

    public Task findById(int id)
    {
        ArrayList<Task> tasks = loadAllTasks();

        for (Task task : tasks)
        {
            if (task.getId() == id)
            {
                return task;
            }
        }
        return null;
    }

    public void deleteById(int id)
    {
        ArrayList<Task> tasks = loadAllTasks();
        for(Task task : tasks)
        {
            if(task.getId() == id){
                tasks.remove(task);
                saveAllTasks(tasks);
                return;
            }
        }
    }

    public void updateTask(Task updatedTask)
    {

        ArrayList<Task> tasks = loadAllTasks();

        for (int i = 0; i < tasks.size(); i++) {

            if (tasks.get(i).getId() == updatedTask.getId()) {

                tasks.set(i, updatedTask);
                saveAllTasks(tasks);
                return;
            }
        }
    }

    public void deleteAllTasks() {
        preferences.edit().remove("tasks_json").apply();
    }
}




