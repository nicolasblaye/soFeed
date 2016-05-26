package fr.infotel.sofeed.bean;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;
import java.util.List;

/**
 * Created by n_bl on 26/05/2016.
 */
public class Employee{

    private int id;
    private String name;
    private String surname;
    private Date birthDate;
    private String email;
    private String telephone;
    private String adresse;
    private String profilPicture;
    private String agency;
    private List<Ticket> tickets;
    private List<Project> projects;
    private List<Event> events;

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
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getProfilPicture() {
        return profilPicture;
    }
    public void setProfilPicture(String profilPicture) {
        this.profilPicture = profilPicture;
    }
    public String getAgency() {
        return agency;
    }
    public void setAgency(String agency) {
        this.agency = agency;
    }
    @JsonIgnore
    public List<Ticket> getTickets() {
        return tickets;
    }
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    @JsonIgnore
    public List<Project> getProjects() {
        return projects;
    }
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    @JsonIgnore
    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
