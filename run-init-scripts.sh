#!/bin/bash
# run-init-scripts.sh

# Esperar a que SQL Server esté completamente listo
echo "Esperando a que SQL Server esté listo..."
/opt/mssql-tools/bin/sqlcmd -S db -U sa -P "$SA_PASSWORD" -Q "SELECT 1" > /dev/null 2>&1
while [ $? -ne 0 ]
do
    echo "SQL Server no está listo aún, reintentando en 5 segundos..."
    sleep 5
    /opt/mssql-tools/bin/sqlcmd -S db -U sa -P "$SA_PASSWORD" -Q "SELECT 1" > /dev/null 2>&1
done
echo "SQL Server está listo."

echo "Ejecutando scripts de inicialización de la base de datos..."

# Ejecutar los scripts en el orden correcto
/opt/mssql-tools/bin/sqlcmd -S db -U sa -P "$SA_PASSWORD" -i /sql-init/01-create-database.sql
/opt/mssql-tools/bin/sqlcmd -S db -U sa -P "$SA_PASSWORD" -i /sql-init/02-create-tables.sql
/opt/mssql-tools/bin/sqlcmd -S db -U sa -P "$SA_PASSWORD" -i /sql-init/03-insert-initial-data.sql

echo "Inicialización de la base de datos completada."
