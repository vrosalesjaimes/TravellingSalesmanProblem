package com.vrj.coh.tsp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "connections")
@Data
public class Connection {
    @EmbeddedId
    private ConnectionId id;

    @MapsId("city1")
    @ManyToOne
    @JoinColumn(name = "id_city_1")
    private City city1;

    @MapsId("city2")
    @ManyToOne
    @JoinColumn(name = "id_city_2")
    private City city2;

    private Double distance;
}
