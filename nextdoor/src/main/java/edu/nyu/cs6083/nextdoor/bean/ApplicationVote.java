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
@Table(name = "applicationvote")
public class ApplicationVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appid")
    @Getter
    @Setter
    private Integer appid;
    @OneToOne
    @JoinColumn(name = "aid")
    @Setter
    @Getter
    private Application application;
    @OneToOne
    @JoinColumn(name = "voteid")
    @Setter
    @Getter
    private User voter;
    @Column(name = "timestamp")
    @Getter
    @Setter
    private Timestamp timestamp;
    @Column(name = "status")
    @Getter
    @Setter
    private Integer status;

    public ApplicationVote() {

    }

    @Override
    public String toString() {
        return "ApplicationVote{" +
            "appid=" + appid +
            ", application=" + application +
            ", voter=" + voter +
            ", timestamp=" + timestamp +
            ", status=" + status +
            '}';
    }
}
