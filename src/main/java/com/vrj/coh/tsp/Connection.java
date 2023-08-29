package com.vrj.coh.tsp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "connections")
@Data
public class Connection {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_city_1")
    private City city1;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_city_2")
    private City city2;

    private Double distance;
}
