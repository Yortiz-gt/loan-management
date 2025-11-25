-- Creación de la tabla EstadoSolicitud si no existe
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'EstadoSolicitud')
    CREATE TABLE EstadoSolicitud (
        EstadoID INT PRIMARY KEY,
        NombreEstado VARCHAR(50) NOT NULL UNIQUE
    );

-- Creación de la tabla TipoPlazo si no existe
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'TipoPlazo')
    CREATE TABLE TipoPlazo (
        PlazoID INT PRIMARY KEY,
        Meses INT NOT NULL,
        TasaInteres DECIMAL(5, 4) NOT NULL
    );

-- Creación de la tabla Cliente si no existe
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Cliente')
    CREATE TABLE Cliente (
        ClienteID INT PRIMARY KEY IDENTITY(1,1),
        Nombre VARCHAR(100) NOT NULL,
        Apellido VARCHAR(100) NOT NULL,
        NumeroIdentificacion VARCHAR(20) NOT NULL UNIQUE,
        FechaNacimiento DATE NOT NULL,
        Direccion VARCHAR(255) NULL,
        CorreoElectronico VARCHAR(100) NOT NULL,
        Telefono VARCHAR(15) NOT NULL,
        UsuarioCreacion VARCHAR(50) NOT NULL,
        FechaCreacion DATETIME NOT NULL DEFAULT GETDATE(),
        UsuarioModificacion VARCHAR(50) NULL,
        FechaModificacion DATETIME NULL
    );

-- Creación de índice en Cliente si no existe
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'UQ_Cliente_Identificacion' AND object_id = OBJECT_ID('Cliente'))
    CREATE UNIQUE INDEX UQ_Cliente_Identificacion ON Cliente (NumeroIdentificacion);

-- Creación de la tabla SolicitudPrestamo si no existe
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'SolicitudPrestamo')
    CREATE TABLE SolicitudPrestamo (
        SolicitudID INT PRIMARY KEY IDENTITY(1,1),
        ClienteID INT NOT NULL,
        MontoSolicitado DECIMAL(18, 2) NOT NULL CHECK (MontoSolicitado > 0),
        PlazoID INT NOT NULL,
        FechaSolicitud DATETIME NOT NULL DEFAULT GETDATE(),
        EstadoID INT NOT NULL,
        DetallesAprobacion VARCHAR(500) NULL,
        UsuarioCreacion VARCHAR(50) NOT NULL,
        FechaCreacion DATETIME NOT NULL DEFAULT GETDATE(),
        UsuarioModificacion VARCHAR(50) NULL,
        FechaModificacion DATETIME NULL,
        FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID),
        FOREIGN KEY (PlazoID) REFERENCES TipoPlazo(PlazoID),
        FOREIGN KEY (EstadoID) REFERENCES EstadoSolicitud(EstadoID)
    );

-- Creación de índice en SolicitudPrestamo si no existe
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Solicitud_Cliente' AND object_id = OBJECT_ID('SolicitudPrestamo'))
    CREATE INDEX IX_Solicitud_Cliente ON SolicitudPrestamo (ClienteID);

-- Creación de la tabla Prestamo si no existe
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Prestamo')
    CREATE TABLE Prestamo (
        PrestamoID INT PRIMARY KEY IDENTITY(1,1),
        SolicitudID INT NOT NULL UNIQUE,
        ClienteID INT NOT NULL,
        MontoPrincipal DECIMAL(18, 2) NOT NULL,
        PlazoMeses INT NOT NULL,
        TasaInteres DECIMAL(5, 4) NOT NULL,
        FechaAprobacion DATETIME NOT NULL DEFAULT GETDATE(),
        MontoPendiente DECIMAL(18, 2) NOT NULL,
        UsuarioCreacion VARCHAR(50) NOT NULL,
        FechaCreacion DATETIME NOT NULL DEFAULT GETDATE(),
        UsuarioModificacion VARCHAR(50) NULL,
        FechaModificacion DATETIME NULL,
        FOREIGN KEY (SolicitudID) REFERENCES SolicitudPrestamo(SolicitudID),
        FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID)
    );

-- Creación de índice en Prestamo si no existe
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'UQ_Prestamo_SolicitudID' AND object_id = OBJECT_ID('Prestamo'))
    CREATE UNIQUE INDEX UQ_Prestamo_SolicitudID ON Prestamo (SolicitudID);

-- Creación de la tabla Pago si no existe
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Pago')
    CREATE TABLE Pago (
        PagoID INT PRIMARY KEY IDENTITY(1,1),
        PrestamoID INT NOT NULL,
        MontoPago DECIMAL(18, 2) NOT NULL CHECK (MontoPago > 0),
        FechaPago DATETIME NOT NULL DEFAULT GETDATE(),
        UsuarioCreacion VARCHAR(50) NOT NULL,
        FechaCreacion DATETIME NOT NULL DEFAULT GETDATE(),
        FOREIGN KEY (PrestamoID) REFERENCES Prestamo(PrestamoID)
    );

-- Creación de índice en Pago si no existe
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Pago_Prestamo' AND object_id = OBJECT_ID('Pago'))
    CREATE INDEX IX_Pago_Prestamo ON Pago (PrestamoID);
