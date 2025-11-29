@echo off
chcp 65001 >nul
title CoWorking Manager
color 0A

echo ===============================
echo    COWORKING MANAGER SYSTEM
echo ===============================
echo.

echo Verificando Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ERRO: Java nao encontrado!
    echo.
    echo Baixando Java automaticamente...
    start https://download.java.com/java/current/jdk-17_windows-x64_bin.exe
    echo.
    echo Por favor:
    echo 1. Instale o Java pelo link que abriu
    echo 2. Execute este arquivo novamente
    pause
    exit
)

echo Java encontrado!
echo Iniciando sistema...
echo.

java -jar CoWorkingManager.jar

if %errorlevel% neq 0 (
    echo.
    echo ERRO: Nao foi possivel executar o programa
    echo Verifique se o arquivo CoWorkingManager.jar esta na mesma pasta
)

pause