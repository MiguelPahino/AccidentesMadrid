package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
        Path csvPath = tempDir.resolve("accidentes.csv");
        String positiveRow = csvRow("S");
        String csv = csvRow("positiva_droga") + System.lineSeparator()
                + csvRow("N") + System.lineSeparator()
                + positiveRow + System.lineSeparator()
                + csvRow("") + System.lineSeparator()
                + "fila incompleta";
        Files.writeString(csvPath, csv);

        assertEquals(List.of(positiveRow), FiltradoCSV.accidentesConDrogas(csvPath));
    }

    private static String csvRow(String drugPositiveValue) {
        String[] columns = new String[19];
        columns[0] = "expediente";
        columns[18] = drugPositiveValue;
        return String.join(";", columns);
    }
}
