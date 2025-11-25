USE [LoanManagementDB];
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Cliente' and xtype='U')
BEGIN
CREATE TABLE Cliente (
                         ClienteID INT PRIMARY KEY IDENTITY(1,1),
                         Nombre NVARCHAR(100) NOT NULL,
                         Apellido NVARCHAR(100) NOT NULL,
                         NumeroIdentificacion NVARCHAR(50) UNIQUE NOT NULL,
                         FechaNacimiento DATE,
                         Direccion NVARCHAR(255),
                         CorreoElectronico NVARCHAR(100) UNIQUE NOT NULL,
                         Telefono NVARCHAR(20),
                         UsuarioCreacion NVARCHAR(50) NOT NULL,
                         FechaCreacion DATETIME NOT NULL,
                         UsuarioModificacion NVARCHAR(50),
                         FechaModificacion DATETIME
);
END;
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='TipoPlazo' and xtype='U')
BEGIN
CREATE TABLE TipoPlazo (
                           PlazoID INT PRIMARY KEY IDENTITY(1,1),
                           Meses INT NOT NULL,
                           TasaInteres DECIMAL(5,4) NOT NULL
);
END;
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='EstadoSolicitud' and xtype='U')
BEGIN
CREATE TABLE EstadoSolicitud (
                                 EstadoID INT PRIMARY KEY IDENTITY(1,1),
                                 NombreEstado NVARCHAR(50) NOT NULL
);
END;
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='SolicitudPrestamo' and xtype='U')
BEGIN
CREATE TABLE SolicitudPrestamo (
                                   SolicitudID INT PRIMARY KEY IDENTITY(1,1),
                                   ClienteID INT NOT NULL,
                                   MontoSolicitado DECIMAL(18,2) NOT NULL,
                                   PlazoID INT NOT NULL,
                                   FechaSolicitud DATETIME NOT NULL,
                                   EstadoID INT NOT NULL,
                                   DetallesAprobacion NVARCHAR(MAX),
                                   UsuarioCreacion NVARCHAR(50) NOT NULL,
                                   FechaCreacion DATETIME NOT NULL,
                                   UsuarioModificacion NVARCHAR(50),
                                   FechaModificacion DATETIME,
                                   FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID),
                                   FOREIGN KEY (PlazoID) REFERENCES TipoPlazo(PlazoID),
                                   FOREIGN KEY (EstadoID) REFERENCES EstadoSolicitud(EstadoID)
);
END;
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Prestamo' and xtype='U')
BEGIN
CREATE TABLE Prestamo (
                          PrestamoID INT PRIMARY KEY IDENTITY(1,1),
                          SolicitudID INT UNIQUE NOT NULL,
                          ClienteID INT NOT NULL,
                          MontoPrincipal DECIMAL(18,2) NOT NULL,
                          PlazoMeses INT NOT NULL,
                          TasaInteres DECIMAL(5,4) NOT NULL,
                          FechaAprobacion DATE NOT NULL,
                          MontoPendiente DECIMAL(18,2) NOT NULL,
                          UsuarioCreacion NVARCHAR(50) NOT NULL,
                          FechaCreacion DATETIME NOT NULL,
                          UsuarioModificacion NVARCHAR(50),
                          FechaModificacion DATETIME,
                          FOREIGN KEY (SolicitudID) REFERENCES SolicitudPrestamo(SolicitudID),
                          FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID)
);
END;
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Pago' and xtype='U')
BEGIN
CREATE TABLE Pago (
                      PagoID INT PRIMARY KEY IDENTITY(1,1),
                      PrestamoID INT NOT NULL,
                      MontoPago DECIMAL(18,2) NOT NULL,
                      FechaPago DATETIME NOT NULL,
                      UsuarioCreacion NVARCHAR(50) NOT NULL,
                      FechaCreacion DATETIME NOT NULL,
                      FOREIGN KEY (PrestamoID) REFERENCES Prestamo(PrestamoID)
);
END;
GO