package com.example.metrognome.rhythmProcessor;

import java.util.ArrayList;
import java.util.Date;

public class rhythmObject {
    private String title;
    private String author;
    private Integer last_bpm;
    private Date last_modified;
    private Date created;

    //Measures is an array of measure objects
    private ArrayList<measureObject> measures;


    //Constructor
    public rhythmObject(String title, String author, Integer top, Integer bottom){
        this.title = title;
        this.author = author;
        this.last_bpm = 60;
        this.last_modified = new Date(System.currentTimeMillis());
        this.created = new Date(System.currentTimeMillis());
        measureObject firstMeasure = new measureObject(top, bottom);
        this.measures.add(firstMeasure);
    }

    //Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getLastBpm() {
        return last_bpm;
    }

    public Date getLastModified() {
        return last_modified;
    }

    public Date getCreated() {
        return created;
    }

    public ArrayList<measureObject> getMeasures(){
        return measures;
    }

    //Setters (We dont have a setter for created because it is set only once)
    public void setAuthor(String newAuthor){
        author = newAuthor;
    }

    public void setTitle(String newTitle){
        title = newTitle;
    }

    public void setLastBpm(Integer bpm){
        last_bpm = bpm;
    }

    public void setLastModified(Date now) {
        last_modified = now;
    }

    //add a measure to the end of the measures list
    public void addMeasure(measureObject measure){
        measures.add(measure);
    }


}
