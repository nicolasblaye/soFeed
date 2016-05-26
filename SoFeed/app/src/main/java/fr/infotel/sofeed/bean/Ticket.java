package fr.infotel.sofeed.bean;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;
import java.util.List;

/**
 * Created by n_bl on 26/05/2016.
 */
public class Ticket {
    private int id;
    private Date startDate;
    private String name;
    private String description;
    private List<Employee> employees;
    private Project project;

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @JsonIgnore
    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @JsonIgnore
    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

}
