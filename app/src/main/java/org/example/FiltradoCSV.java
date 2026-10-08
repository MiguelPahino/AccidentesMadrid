package org.example;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FiltradoCSV {

    private static final String CSV_FILE = "accidentesTraficoMadrid.csv";

    public static List<String> accidentesConDrogas() throws IOException {
        return accidentesConDrogas(findCsvPath(Path.of("").toAbsolutePath()));
    }

    static List<String> accidentesConDrogas(Path csvPath) throws IOException {
        List<String> accidents = new ArrayList<>();

        List<String> lines = Files.readAllLines(csvPath);
            for(String line : lines){
                String[] lineList = line.split(";", -1);
                if(lineList.length > 18 && "S".equals(lineList[18].trim())){
                    accidents.add(line);
                }
            }
        

        return accidents;
    }



    static Path findCsvPath(Path startDirectory) throws FileNotFoundException {
        for (Path directory = startDirectory.toAbsolutePath();
                directory != null;
                directory = directory.getParent()) {
            Path candidate = directory.resolve(Path.of("data", CSV_FILE));
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }

        throw new FileNotFoundException("No se encontró el archivo data/" + CSV_FILE);
    }
}
