package fr.infotel.sofeed.bean;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;
import java.util.List;

/**
 * Created by n_bl on 26/05/2016.
 */
public class Project {
    private int id;
    private String name;
    private Date startDate;
    private String information;
    private List<Document> documents;
    private List<Employee> team;
    private List<Ticket> tickets;

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
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getInformation() {
        return information;
    }
    public void setInformation(String information) {
        this.information = information;
    }
    @JsonIgnore
    public List<Employee> getTeam() {
        return team;
    }
    public void setTeam(List<Employee> team) {
        this.team = team;
    }
    @JsonIgnore
    public List<Document> getDocuments() {
        return documents;
    }
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
    @JsonIgnore
    public List<Ticket> getTickets() {
        return tickets;
    }
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
