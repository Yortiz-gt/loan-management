IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'LoanManagementDB')
BEGIN
    CREATE DATABASE [LoanManagementDB];
END;
GO
USE [LoanManagementDB];
GO