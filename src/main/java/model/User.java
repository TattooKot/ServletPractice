package model;

import javax.persistence.*;

@Table(name = "users", indexes = {
        @Index(name = "users_ipaddress_key", columnList = "ipaddress", unique = true)
})
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ipaddress", nullable = false, length = 15)
    private String ipaddress;

    public User() {
    }

    public User(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", ipaddress='" + ipaddress + '\'' +
                '}';
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}