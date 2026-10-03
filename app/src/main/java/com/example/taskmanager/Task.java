package com.example.taskmanager;
import java.io.Serializable;

public abstract class Task implements Serializable, Rewardable
{
    private int id;
    private String title;
    private String subject;
    private String priority;
    private String dueDate;
    private boolean done;

    public Task(int id,String title,String subject,String priority,String dueDate,boolean done)
    {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.dueDate = dueDate;
        this.priority = priority;
        this.done = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }


    @Override
    public abstract int getPoints();
    public abstract String getTypeName();

    protected int getPriorityBonus() {
        if (priority.equals("גבוהה")) {
            return 5;
        } else if (priority.equals("בינונית")) {
            return 3;
        }
        else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return title + "\n"
                + getTypeName() + " · "
                + subject + " · הגשה: " + dueDate;
    }

}

