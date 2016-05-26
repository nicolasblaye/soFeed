package fr.infotel.sofeed.bean;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;
import java.util.List;

/**
 * Created by n_bl on 26/05/2016.
 */
public class Event {
    private int id;
    private String name;
    private String type;
    private String description;
    private Date startDate;
    private Date endDate;
    private List<Employee> participants;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @JsonIgnore
    public List<Employee> getParticipants() {
        return participants;
    }
    public void setParticipants(List<Employee> participants) {
        this.participants = participants;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
