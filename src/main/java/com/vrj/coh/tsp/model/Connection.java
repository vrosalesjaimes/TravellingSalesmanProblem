package com.vrj.coh.tsp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "connections")
@Data
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_city_1")
    private City city1;

    @ManyToOne
    @JoinColumn(name = "id_city_2")
    private City city2;

    private Double distance;
}
