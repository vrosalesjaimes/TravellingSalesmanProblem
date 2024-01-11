# Travelling Salesman Problem

Esta es una implementacion de Aceptación por umbrales para aproximar soluciones para TSP. El sistema esta construido con java 17 y hace uso de maven.

## Compilación 
Desde línea de comandos y haciendo uso de maven 

```bash
mvn install
```

# Ejecución

El sistema recibe dos parametros:
- \<Archivo\>: archivo con numeros separados por comas, estos deben ir entre 1 y 1092 sin repetirse:
- \<Semilla\>: numero entero 

Se ejecuta de la siguiente forma:
```bash
java -jar target/TravellingSalesmanProblem-jar-with-dependencies.jar <Archivo> <Semilla>
```

Por ejemplo

```bash
java -jar target/TravellingSalesmanProblem-jar-with-dependencies.jar inputs/input-40.tsp 5123 
java -jar target/TravellingSalesmanProblem-jar-with-dependencies.jar inputs/input-150.tsp 82 
```
