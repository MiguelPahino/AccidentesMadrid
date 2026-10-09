package org.example.source;

import org.example.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

public class Source {
    public static List<String> accidentesConDrogas() throws IOException {
        List<String> accidents = new ArrayList<>();
        List<String> lines = Files.readAllLines(Utils.findCsvPath(Path.of("").toAbsolutePath()));
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = Utils.separarColumnasCsv(line);
            if (columns.length > 18 && "1".equals(columns[18].trim())) {
                accidents.add(line);
            }
        }
        return accidents;
    }


    public static Map<String, Integer> accidentesAgrupadosPorSexo() throws IOException {
        Map<String, Set<String>> accidentIdsBySex = new LinkedHashMap<>();
        List<String> lines = Files.readAllLines(Utils.findCsvPath(Path.of("").toAbsolutePath()));
        for (int i = 1; i < lines.size(); i++) {
            String[] columns = Utils.separarColumnasCsv(lines.get(i));
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
        List<String> lines = Files.readAllLines(Utils.findCsvPath(Path.of("").toAbsolutePath()));
        for (int i = 1; i < lines.size(); i++) {
            String[] columns = Utils.separarColumnasCsv(lines.get(i));
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
        List<String> lines = Files.readAllLines(Utils.findCsvPath(Path.of("").toAbsolutePath()));
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = Utils.separarColumnasCsv(line);
            if (columns.length > 7
                    && "Atropello a persona".equalsIgnoreCase(columns[7].trim())) {
                accidents.add(line);
            }
        }
        return accidents;
    }
}