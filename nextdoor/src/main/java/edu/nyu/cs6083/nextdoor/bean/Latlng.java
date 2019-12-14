package edu.nyu.cs6083.nextdoor.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
public class Latlng {

    @Getter
    @Setter
    private Double lat;

    @Getter
    @Setter
    private Double lng;


    @Override
    public String toString() {
        return "Latlng{" +
            "lat=" + lat +
            ", lng=" + lng +
            '}';
    }
}
