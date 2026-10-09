package org.example.utils;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String CSV_FILE = "2025_Accidentalidad.csv";

    public static Path findCsvPath(Path startDirectory) throws FileNotFoundException {
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

    public static String[] separarColumnasCsv(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder column = new StringBuilder();
        boolean entreComillas = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (entreComillas && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    column.append('"');
                    i++;
                } else {
                    entreComillas = !entreComillas;
                }
            } else if (c == ';' && !entreComillas) {
                columns.add(column.toString());
                column.setLength(0);
            } else {
                column.append(c);
            }
        }

        columns.add(column.toString());
        return columns.toArray(new String[0]);
    }
}