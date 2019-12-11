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
@Table(name = "neighborhood")
public class Neighborhood {

    public Neighborhood() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "nid")
    private Integer nid;

    @Getter
    @Setter
    @Column(name = "nname")
    private String nname;

    @Getter
    @Setter
    @Column(name = "sw_lng")
    private Float sw_lng;

    @Getter
    @Setter
    @Column(name = "sw_lat")
    private Float sw_lat;

    @Getter
    @Setter
    @Column(name = "ne_lng")
    private Float ne_lng;

    @Getter
    @Setter
    @Column(name = "ne_lat")
    private Float ne_lat;

    @Override
    public String toString() {
        return "Neighborhood{" +
            "nid=" + nid +
            ", nname='" + nname + '\'' +
            ", sw_lng=" + sw_lng +
            ", sw_lat=" + sw_lat +
            ", ne_lng=" + ne_lng +
            ", ne_lat=" + ne_lat +
            '}';
    }
}
