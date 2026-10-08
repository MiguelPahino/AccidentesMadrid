package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FiltradoCSVTest {

    @TempDir
    Path tempDir;

    @Test
    void findCsvPathFindsCsvInParentDataDirectory() throws IOException {
        Path csvPath = tempDir.resolve("data").resolve("accidentesTraficoMadrid.csv");
        Files.createDirectories(csvPath.getParent());
        Files.createFile(csvPath);
        Path nestedDirectory = Files.createDirectories(tempDir.resolve("app/src"));

        assertEquals(csvPath, FiltradoCSV.findCsvPath(nestedDirectory));
    }

    @Test
    void findCsvPathThrowsWhenCsvDoesNotExist() {
        assertThrows(FileNotFoundException.class, () -> FiltradoCSV.findCsvPath(tempDir));
    }

    @Test
    void accidentesConDrogasReturnsOnlyRowsMarkedAsPositive() throws IOException {
        List<String> expected = new java.util.ArrayList<>();
        for (String line : readCsvDataLines()) {
            String[] columns = line.split(";", -1);
            if (columns.length > 18 && "S".equals(columns[18].trim())) {
                expected.add(line);
            }
        }

        assertEquals(expected, FiltradoCSV.accidentesConDrogas());
    }

    @Test
    void accidentesAgrupadosPorSexoCountsEachAccidentOncePerSex() throws IOException {
        Map<String, Set<String>> accidentIdsBySex = new LinkedHashMap<>();
        for (String line : readCsvDataLines()) {
            String[] columns = line.split(";", -1);
            if (columns.length > 12 && !columns[0].trim().isEmpty()
                    && !columns[12].trim().isEmpty()) {
                accidentIdsBySex.computeIfAbsent(columns[12].trim(), key -> new HashSet<>())
                        .add(columns[0].trim());
            }
        }

        Map<String, Integer> expected = new LinkedHashMap<>();
        accidentIdsBySex.forEach((sex, accidentIds) -> expected.put(sex, accidentIds.size()));
        assertEquals(expected, FiltradoCSV.accidentesAgrupadosPorSexo());
    }

    @Test
    void accidentesAgrupadosPorMesCountsEachAccidentOnce() throws IOException {
        Map<String, Set<String>> accidentIdsByMonth = new LinkedHashMap<>();
        for (String line : readCsvDataLines()) {
            String[] columns = line.split(";", -1);
            if (columns.length > 1 && !columns[0].trim().isEmpty()
                    && columns[1].matches("\\d{2}/(0[1-9]|1[0-2])/\\d{4}")) {
                int monthNumber = Integer.parseInt(columns[1].substring(3, 5));
                String month = Month.of(monthNumber)
                        .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
                accidentIdsByMonth.computeIfAbsent(month, key -> new HashSet<>())
                        .add(columns[0].trim());
            }
        }

        Map<String, Integer> expected = new LinkedHashMap<>();
        accidentIdsByMonth.forEach((month, accidentIds) -> expected.put(month, accidentIds.size()));
        assertEquals(expected, FiltradoCSV.accidentesAgrupadosPorMes());
    }

    @Test
    void accidentesConAtropelloAPersonasReturnsOnlyPedestrianCollisions() throws IOException {
        List<String> expected = new java.util.ArrayList<>();
        for (String line : readCsvDataLines()) {
            String[] columns = line.split(";", -1);
            if (columns.length > 7
                    && "Atropello a persona".equalsIgnoreCase(columns[7].trim())) {
                expected.add(line);
            }
        }

        assertEquals(expected, FiltradoCSV.accidentesConAtropelloAPersonas());
    }

    private List<String> readCsvDataLines() throws IOException {
        Path csvPath = FiltradoCSV.findCsvPath(Path.of("").toAbsolutePath());
        List<String> lines = Files.readAllLines(csvPath);
        return lines.size() < 2 ? List.of() : lines.subList(1, lines.size());
    }
}
