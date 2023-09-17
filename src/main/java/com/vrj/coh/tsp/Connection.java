package com.vrj.coh.tsp;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    private int city1;

    private int city2;

    private Double distance;

    private static ConnectorDB CONNECTORDB = new ConnectorDB();

    public static Connection findByCity1AndCity2(City city1, City city2){
        String query = "SELECT * FROM connections " +
                        "WHERE (id_city_1 = " + city1.getId() + " AND id_city_2 = " + city2.getId() + ");"; 
        ResultSet resultSet = CONNECTORDB.executeQuery(query);;
        try{
            if(resultSet.next()){
                Connection connection = new Connection();
                connection.setCity1(resultSet.getInt("id_city_1"));
                connection.setCity2(resultSet.getInt("id_city_2"));
                connection.setDistance(resultSet.getDouble("distance"));
                return connection;
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar la consulta", e);
        }
    }
}
