#!/bin/bash

# Diretório das pastas dos projetos
PROJECT_DIR="dataset"

# Diretório dos arquivos .csv
CSV_DIR="../jsminer-out"

# Loop através de cada pasta no diretório do projeto
for PROJECT_FOLDER in "$PROJECT_DIR"/*; do
  # Verifica se é uma pasta
  if [ -d "$PROJECT_FOLDER" ]; then
    # Obtém o nome da pasta sem o caminho
    PROJECT_NAME=$(basename "$PROJECT_FOLDER")
    
    # Constrói o caminho do arquivo .csv correspondente
    CSV_FILE="$CSV_DIR/$PROJECT_NAME.csv"
    
    # Verifica se o arquivo .csv existe
    if [ -f "$CSV_FILE" ]; then
      # Remove a pasta do projeto
      rm -rf "$PROJECT_FOLDER"
      echo "Removido $PROJECT_FOLDER porque $CSV_FILE existe."
    fi
  fi
done

