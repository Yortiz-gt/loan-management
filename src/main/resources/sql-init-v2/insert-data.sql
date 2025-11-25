-- Inserción de datos iniciales en EstadoSolicitud si no existen
IF NOT EXISTS (SELECT 1 FROM EstadoSolicitud WHERE EstadoID = 1)
    INSERT INTO EstadoSolicitud (EstadoID, NombreEstado) VALUES (1, 'EN_PROCESO');
IF NOT EXISTS (SELECT 1 FROM EstadoSolicitud WHERE EstadoID = 2)
    INSERT INTO EstadoSolicitud (EstadoID, NombreEstado) VALUES (2, 'APROBADO');
IF NOT EXISTS (SELECT 1 FROM EstadoSolicitud WHERE EstadoID = 3)
    INSERT INTO EstadoSolicitud (EstadoID, NombreEstado) VALUES (3, 'RECHAZADO');

-- Inserción de datos iniciales en TipoPlazo si no existen
IF NOT EXISTS (SELECT 1 FROM TipoPlazo WHERE PlazoID = 1)
    INSERT INTO TipoPlazo (PlazoID, Meses, TasaInteres) VALUES (1, 12, 0.0800);
IF NOT EXISTS (SELECT 1 FROM TipoPlazo WHERE PlazoID = 2)
    INSERT INTO TipoPlazo (PlazoID, Meses, TasaInteres) VALUES (2, 24, 0.0750);
IF NOT EXISTS (SELECT 1 FROM TipoPlazo WHERE PlazoID = 3)
    INSERT INTO TipoPlazo (PlazoID, Meses, TasaInteres) VALUES (3, 36, 0.0700);
