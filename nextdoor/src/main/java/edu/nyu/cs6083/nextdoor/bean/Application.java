package edu.nyu.cs6083.nextdoor.bean;


import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid")
    @Getter
    @Setter
    private Integer aid;
    @OneToOne
    @JoinColumn(name = "uid")
    @Setter
    @Getter
    private User user;
    @OneToOne
    @JoinColumn(name = "bid")
    @Setter
    @Getter
    private Block block;
    @Column(name = "timestamp")
    @Getter
    @Setter
    private Timestamp timestamp;
    @Column(name = "status")
    @Getter
    @Setter
    private Integer status;


    public Application() {

    }

    @Override
    public String toString() {
        return "Application{" +
            "aid=" + aid +
            ", user=" + user +
            ", block=" + block +
            ", timestamp=" + timestamp +
            ", status=" + status +
            '}';
    }
}
