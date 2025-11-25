USE [LoanManagementDB];
GO

-- Insertar Estados de Solicitud si no existen
IF NOT EXISTS (SELECT 1 FROM EstadoSolicitud WHERE NombreEstado = 'EN_PROCESO')
BEGIN
INSERT INTO EstadoSolicitud (NombreEstado) VALUES ('EN_PROCESO');
END;
IF NOT EXISTS (SELECT 1 FROM EstadoSolicitud WHERE NombreEstado = 'APROBADO')
BEGIN
INSERT INTO EstadoSolicitud (NombreEstado) VALUES ('APROBADO');
END;
IF NOT EXISTS (SELECT 1 FROM EstadoSolicitud WHERE NombreEstado = 'RECHAZADO')
BEGIN
INSERT INTO EstadoSolicitud (NombreEstado) VALUES ('RECHAZADO');
END;
GO

-- Insertar Tipos de Plazo si no existen
IF NOT EXISTS (SELECT 1 FROM TipoPlazo WHERE Meses = 12 AND TasaInteres = 0.0500)
BEGIN
INSERT INTO TipoPlazo (Meses, TasaInteres) VALUES (12, 0.0500);
END;
IF NOT EXISTS (SELECT 1 FROM TipoPlazo WHERE Meses = 24 AND TasaInteres = 0.0450)
BEGIN
INSERT INTO TipoPlazo (Meses, TasaInteres) VALUES (24, 0.0450);
END;
IF NOT EXISTS (SELECT 1 FROM TipoPlazo WHERE Meses = 36 AND TasaInteres = 0.0400)
BEGIN
INSERT INTO TipoPlazo (Meses, TasaInteres) VALUES (36, 0.0400);
END;
GO