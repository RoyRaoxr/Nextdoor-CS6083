package edu.nyu.cs6083.nextdoor.bean;


import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mid")
    @Getter
    @Setter
    private Integer mid;

    @OneToOne
    @JoinColumn(name = "author")
    @Getter
    @Setter
    private User author;

    @ManyToOne
    @JoinColumn(name = "tid")
    @Setter
    @Getter
    private MsgThread msgThread;

    @Column(name = "title")
    @Getter
    @Setter
    private String title;

    @Column(name = "timestamp")
    @Getter
    @Setter
    private Timestamp timestamp;

    @Column(name = "text")
    @Getter
    @Setter
    private String text;


    @OneToOne
    @JoinColumn(name = "replyid")
    @Getter
    @Setter
    private Message replyid;

    @Column(name = "lat")
    @Getter
    @Setter
    private Float lat;

    @Column(name = "lng")
    @Getter
    @Setter
    private Float lng;


    @Override
    public String toString() {
        return "Message{" +
            "mid=" + mid +
            ", author=" + author +
            ", msgThread=" + msgThread +
            ", title='" + title + '\'' +
            ", timestamp=" + timestamp +
            ", text='" + text + '\'' +
            ", replyid=" + replyid +
            ", lat=" + lat +
            ", lng=" + lng +
            '}';
    }
}
