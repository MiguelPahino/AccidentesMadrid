package org.example;

import org.example.repository.Source;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class FiltradoCSV {

    public static void main(String[] args) throws IOException {
        Map<String, Integer> accidentsBySex = Source.accidentesAgrupadosPorSexo();
        Map<String, Integer> accidentsByMonth = Source.accidentesAgrupadosPorMes();
        List<String> drugPositiveAccidents = Source.accidentesConDrogas();
        List<String> pedestrianAccidents = Source.accidentesConAtropelloAPersonas();

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


}
