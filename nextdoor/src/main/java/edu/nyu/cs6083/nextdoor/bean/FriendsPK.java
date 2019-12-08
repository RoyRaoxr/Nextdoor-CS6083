package edu.nyu.cs6083.nextdoor.bean;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
public class FriendsPK implements Serializable {

    public FriendsPK() {

    }

    public FriendsPK(Integer userid, Integer friendid) {
        this.userid = userid;
        this.friendid = friendid;
    }

    @Getter
    @Setter
    private Integer userid;

    @Getter
    @Setter
    private Integer friendid;
}
