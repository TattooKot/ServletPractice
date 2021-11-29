package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users", indexes = {
        @Index(name = "users_username_key", columnList = "username", unique = true)
})
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Event> events = new ArrayList<>();

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> eventList) {
        this.events = eventList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}