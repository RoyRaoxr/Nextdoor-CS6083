package edu.nyu.cs6083.nextdoor.bean;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "block")
public class Block {

    @Id
    @Getter
    @Setter
    @Column(name = "bid")
    private Integer bid;
    @OneToOne
    @JoinColumn(name = "nid")
    @Getter
    @Setter
    private Neighborhood neighborhood;
    @Getter
    @Setter
    @Column(name = "bname")
    private String bname;
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
    @Column(name = "ne_lat")
    private Float ne_lat;
    @Getter
    @Setter
    @Column(name = "ne_lng")
    private Float ne_lng;

    public Block() {
    }

    @Override
    public String toString() {
        return "Block{" +
            "bid=" + bid +
            ", neighborhood=" + neighborhood +
            ", bname='" + bname + '\'' +
            ", sw_lng=" + sw_lng +
            ", sw_lat=" + sw_lat +
            ", ne_lat=" + ne_lat +
            ", ne_lng=" + ne_lng +
            '}';
    }
}
