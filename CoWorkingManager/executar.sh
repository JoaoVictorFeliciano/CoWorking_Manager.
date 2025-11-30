#!/bin/bash

echo "==============================="
echo "   COWORKING MANAGER SYSTEM"
echo "==============================="
echo ""

if ! command -v java &> /dev/null; then
    echo "ERRO: Java não encontrado!"
    echo "Instale o Java: sudo apt install openjdk-17-jdk"
    exit 1
fi

echo "Java encontrado!"
echo "Iniciando sistema..."
java -jar CoWorkingManager.jar