package fr.infotel.sofeed.bean;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

/**
 * Created by n_bl on 26/05/2016.
 */
public class Document {
    private int id;
    private String name;
    private Date date;
    private String path;
    private Project project;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    @JsonIgnore
    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

}
