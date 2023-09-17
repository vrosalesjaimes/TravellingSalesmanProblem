package com.vrj.coh.tsp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectorDB {
    
    private Connection connection;

    public ConnectorDB() {
        try {
            String url = "jdbc:sqlite:./db/tsp.sqlite3";
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar a la base de datos SQLite", e);
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar la consulta", e);
        }
    }

    public void close() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar la conexión", e);
        }
    }
}
