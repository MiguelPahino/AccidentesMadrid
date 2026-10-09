package org.example.repository;

import jdk.jshell.execution.Util;
import org.example.model.Accidente;
import org.example.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class repositoryAccidente {

    private List<Accidente> accidentes;

    private List<Accidente> getListaAccidentes(Path ruta) throws IOException {

        List<String> lineas = Files.readAllLines(Utils.findCsvPath(Path.of("").toAbsolutePath()));
        return lineas.stream().map(x -> crearAccidente(x)).toList();
    }

    private Accidente crearAccidente(String lineaCSV){
        String[] arrayLinea = Utils.separarColumnasCsv(lineaCSV);
        return new Accidente(arrayLinea[0],arrayLinea[1],arrayLinea[2],arrayLinea[3],arrayLinea[4],arrayLinea[5],arrayLinea[6],arrayLinea[7],arrayLinea[8],
                arrayLinea[9],arrayLinea[10],arrayLinea[11],arrayLinea[12],arrayLinea[13],arrayLinea[14],
        arrayLinea[15],arrayLinea[16],arrayLinea[17],arrayLinea[18]);
    }
}
