package com.vrj.coh.tsp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ConnectionId implements Serializable {
    @Column(name = "id_city_1")
    private Long idCity1;

    @Column(name = "id_city_2")
    private Long idCity2;
}
