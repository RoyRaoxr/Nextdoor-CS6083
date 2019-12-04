package edu.nyu.cs6083.nextdoor.bean;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
public class User {

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    @Getter
    @Setter
    private Integer uid;

    @Getter
    @Setter
    @Column(name = "username")
    private String username;

    @Getter
    @Setter
    @Column(name = "password")
    private String password;

    @Getter
    @Setter
    @Column(name = "firstname")
    private String firstname;

    @Getter
    @Setter
    @Column(name = "lastname")
    private String lastname;

    @Getter
    @Setter
    @Column(name = "ustreet")
    private String ustreet;

    @Getter
    @Setter
    @Column(name = "ucity")
    private String ucity;

    @Getter
    @Setter
    @Column(name = "ustate")
    private String ustate;

    @Getter
    @Setter
    @Column(name = "profile")
    private String profile;

    @Getter
    @Setter
    @Column(name = "photo")
    private String photo;

    @Getter
    @Setter
    @Column(name = "notify")
    private String notify;

    @Override
    public String toString() {
        return "User{" +
            "uid=" + uid +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", ustreet='" + ustreet + '\'' +
            ", ucity='" + ucity + '\'' +
            ", ustate='" + ustate + '\'' +
            ", profile='" + profile + '\'' +
            ", photo='" + photo + '\'' +
            ", notify='" + notify + '\'' +
            '}';
    }
}
