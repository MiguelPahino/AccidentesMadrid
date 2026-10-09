package org.example.model;

public record Accidente(
        String numExpediente,
        String fecha,
        String hora,
        String localizacion,
        String numero,
        String codDistrito,
        String distrito,
        String tipoAccidente,
        String estadoMeteorologico,
        String tipoVehiculo,
        String tipoPersona,
        String rangoEdad,
        String sexo,
        String codLesividad,
        String lesividad,
        String coordenadaXUtm,
        String coordenadaYUtm,
        String positivaAlcohol,
        String positivaDroga
) {}
