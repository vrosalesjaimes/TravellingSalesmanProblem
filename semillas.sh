#!/bin/bash

# Nombre del archivo JAR y la entrada
JAR_FILE="target/TravellingSalesmanProblem-jar-with-dependencies.jar"
INPUT_FILE="inputs/input-150.tsp"

# Rango de semillas que deseas probar
SEMILLA_INICIAL=1
SEMILLA_FINAL=1000

# Nombre del archivo de salida
OUTPUT_FILE="salida9-150.csv"

# Función para ejecutar el comando con timeout y registro
execute_with_timeout() {
    semilla="$1"
    comando="java -jar \"$JAR_FILE\" \"$INPUT_FILE\" \"$semilla\""
    timeout 10m java -jar "$JAR_FILE" "$INPUT_FILE" "$semilla" 2>&1 | tee -a "$OUTPUT_FILE"
    echo "------------------------------------"
}

# Bucle for para ejecutar el comando con diferentes semillas
for ((semilla = SEMILLA_INICIAL; semilla <= SEMILLA_FINAL; semilla++))
do
    execute_with_timeout "$semilla"
done

echo "Proceso completo. Los resultados se han guardado en $OUTPUT_FILE"