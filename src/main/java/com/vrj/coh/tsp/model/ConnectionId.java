package com.vrj.coh.tsp.model;


import java.io.Serializable;
import java.util.Objects;

public class ConnectionId implements Serializable {

    private Long city1;
    private Long city2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionId that = (ConnectionId) o;
        return Objects.equals(city1, that.city1) &&
                Objects.equals(city2, that.city2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city1, city2);
    }

}
