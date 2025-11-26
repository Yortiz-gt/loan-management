#!/bin/bash
set -e

echo "Esperando a que SQL Server esté listo..."

# Esperar hasta que SQL Server acepte conexiones
until /opt/mssql-tools/bin/sqlcmd -S db -U sa -P "$SA_PASSWORD" -Q "SELECT 1" &> /dev/null
do
    echo "SQL Server no está listo, esperando..."
    sleep 5
done

echo "SQL Server listo. Ejecutando scripts..."

for f in /sql-init/*.sql
do
  echo "Ejecutando $f..."
  /opt/mssql-tools/bin/sqlcmd -S db -U sa -P "$SA_PASSWORD" -d master -i "$f"
done

echo "Scripts ejecutados correctamente."
