package com.example.taskmanager;
public class ExamTask extends Task
{
    private int topics;
    public ExamTask(int id,String title,String subject,String priority,String dueDate, boolean done,int topics)
    {
        super(id,title,subject,priority,dueDate,done);
        this.topics = topics;
    }

    public int getTopics() {
        return topics;
    }

    public void setTopics(int topics) {
        this.topics = topics;
    }

    @Override
    public String getTypeName(){
        return "מבחן";
    }

    @Override
    public int getPoints(){
        return getPriorityBonus() + 10 + 5*this.topics;
    }

}


