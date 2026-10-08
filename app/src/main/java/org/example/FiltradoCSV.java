package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FiltradoCSV {

    private static final String CSV_FILE = "accidentesTraficoMadrid.csv";
    private static final int POSITIVA_DROGA_COLUMN = 18;

    public static List<String> accidentesConDrogas() throws IOException {
        return accidentesConDrogas(findCsvPath(Path.of("").toAbsolutePath()));
    }

    static List<String> accidentesConDrogas(Path csvPath) throws IOException {
        List<String> accidents = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";", -1);
                if (columns.length > POSITIVA_DROGA_COLUMN
                        && "S".equalsIgnoreCase(columns[POSITIVA_DROGA_COLUMN].trim())) {
                    accidents.add(line);
                }
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
