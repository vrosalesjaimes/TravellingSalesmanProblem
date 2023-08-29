package com.vrj.coh.tsp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "connections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connection {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_city_1")
    private City city1;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_city_2")
    private City city2;

    /* Distance between two cities */
    private Double distance;
}