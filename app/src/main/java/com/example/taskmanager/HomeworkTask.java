package com.example.taskmanager;

public class HomeworkTask extends Task
{
    private int exercises;

    public HomeworkTask(int id,String title,String subject,String priority,String dueDate, boolean done, int exercises){
        super(id,title,subject,priority,dueDate, done);
        this.exercises = exercises;
    }

    public int getExercises() {
        return exercises;
    }

    public void setExercises(int exercises) {
        this.exercises = exercises;
    }

    @Override
    public String getTypeName(){
        return "שיעורי בית";
    }

    @Override
    public int getPoints(){
        return getPriorityBonus() + 2*this.exercises;
    }

}
