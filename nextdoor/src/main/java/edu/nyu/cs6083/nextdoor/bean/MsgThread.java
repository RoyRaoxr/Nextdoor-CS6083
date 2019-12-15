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
@Table(name = "thread")
public class MsgThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid")
    @Getter
    @Setter
    private Integer tid;
    @Column(name = "subject")
    @Getter
    @Setter
    private String subject;
    @Column(name = "type")
    @Getter
    @Setter
    private Integer type;

    public MsgThread() {

    }

    @Override
    public String toString() {
        return "Thread{" +
            "tid=" + tid +
            ", subject='" + subject + '\'' +
            ", type=" + type +
            '}';
    }
}
