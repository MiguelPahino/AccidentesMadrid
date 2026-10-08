package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class FiltradoCSV {

    private static final String CSV_FILE = "accidentesTraficoMadrid.csv";

    public static void main(String[] args) throws IOException {
        Map<String, Integer> accidentsBySex = accidentesAgrupadosPorSexo();
        Map<String, Integer> accidentsByMonth = accidentesAgrupadosPorMes();
        List<String> drugPositiveAccidents = accidentesConDrogas();
        List<String> pedestrianAccidents = accidentesConAtropelloAPersonas();

        System.out.println("=== Estadisticas de accidentes de trafico de Madrid ===");

        System.out.println("\nAccidentes agrupados por sexo:");
        for (Map.Entry<String, Integer> entry : accidentsBySex.entrySet()) {
            System.out.printf("  %-15s %d%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nAccidentes agrupados por mes:");
        for (Map.Entry<String, Integer> entry : accidentsByMonth.entrySet()) {
            System.out.printf("  %-15s %d%n", entry.getKey(), entry.getValue());
        }

        System.out.printf("%nFilas con resultado positivo en drogas: %d%n",
                drugPositiveAccidents.size());
        System.out.printf("Filas de atropello a personas: %d%n", pedestrianAccidents.size());
    }

    public static List<String> accidentesConDrogas() throws IOException {
        List<String> accidents = new ArrayList<>();
        List<String> lines = Files.readAllLines(findCsvPath(Path.of("").toAbsolutePath()));
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = line.split(";", -1);
            if (columns.length > 18 && "S".equals(columns[18].trim())) {
                accidents.add(line);
            }
        }
        return accidents;
    }

    
    public static Map<String, Integer> accidentesAgrupadosPorSexo() throws IOException {
        Map<String, Set<String>> accidentIdsBySex = new LinkedHashMap<>();
        List<String> lines = Files.readAllLines(findCsvPath(Path.of("").toAbsolutePath()));
        for (int i = 1; i < lines.size(); i++) {
            String[] columns = lines.get(i).split(";", -1);
            if (columns.length > 12) {
                String accidentId = columns[0].trim();
                String sex = columns[12].trim();
                if (!accidentId.isEmpty() && !sex.isEmpty()) {
                    accidentIdsBySex.computeIfAbsent(sex, key -> new HashSet<>())
                            .add(accidentId);
                }
            }
        }

        Map<String, Integer> countsBySex = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : accidentIdsBySex.entrySet()) {
            countsBySex.put(entry.getKey(), entry.getValue().size());
        }
        return countsBySex;
    }

    public static Map<String, Integer> accidentesAgrupadosPorMes() throws IOException {
        Map<String, Set<String>> accidentIdsByMonth = new LinkedHashMap<>();
        List<String> lines = Files.readAllLines(findCsvPath(Path.of("").toAbsolutePath()));
        for (int i = 1; i < lines.size(); i++) {
            String[] columns = lines.get(i).split(";", -1);
            if (columns.length > 1 && !columns[0].trim().isEmpty()
                    && columns[1].matches("\\d{2}/(0[1-9]|1[0-2])/\\d{4}")) {
                int monthNumber = Integer.parseInt(columns[1].substring(3, 5));
                String month = Month.of(monthNumber)
                        .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
                accidentIdsByMonth.computeIfAbsent(month, key -> new HashSet<>())
                        .add(columns[0].trim());
            }
        }

        Map<String, Integer> countsByMonth = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : accidentIdsByMonth.entrySet()) {
            countsByMonth.put(entry.getKey(), entry.getValue().size());
        }
        return countsByMonth;
    }

    public static List<String> accidentesConAtropelloAPersonas() throws IOException {
        List<String> accidents = new ArrayList<>();
        List<String> lines = Files.readAllLines(findCsvPath(Path.of("").toAbsolutePath()));
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = line.split(";", -1);
            if (columns.length > 7
                    && "Atropello a persona".equalsIgnoreCase(columns[7].trim())) {
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
