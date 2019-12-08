package edu.nyu.cs6083.nextdoor.bean;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "friends")
public class Friends {

    @EmbeddedId
    private FriendsPK pk;

    @Getter
    @Setter
    @Column(name = "status")
    private Integer status;

    @Getter
    @Setter
    @Column(name = "nid")
    private Integer nid;


}
