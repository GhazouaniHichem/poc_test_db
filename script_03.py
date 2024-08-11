import os
import re
import csv
import pandas as pd

# Chemin du répertoire racine du projet Java et le fichier CSV
project_root = "hiber"
excel_file_path = "table.xlsx"

def load_table_column_names_from_excel(excel_file):
    # Lire le fichier Excel
    df = pd.read_excel(excel_file)

    # Assurer que le fichier contient les colonnes 'table_name' et 'column_name'
    if 'table_name' not in df.columns or 'column_name' not in df.columns:
        raise ValueError("Le fichier Excel doit contenir les colonnes 'table_name' et 'column_name'.")

    # Extraire les paires (table_name, column_name)
    table_column_names = list(df[['table_name', 'column_name']].itertuples(index=False, name=None))
    return table_column_names

# Fonction pour rechercher les mappings table-colonne dans les fichiers Java
def find_table_column_mappings(root_dir):
    mappings = {}
    column_pattern = re.compile(r'@Column\s*\(\s*name\s*=\s*"(.*?)"\s*(?:[^)]*)\)', re.DOTALL)
    table_pattern = re.compile(r'@Table\s*\(\s*name\s*=\s*"(.*?)"\s*\)', re.DOTALL)
    
    for subdir, _, files in os.walk(root_dir):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(subdir, file)
                with open(file_path, 'r', encoding='utf-8') as java_file:
                    content = java_file.read()

                    # Rechercher l'annotation @Table
                    table_match = table_pattern.search(content)
                    if table_match:
                        table_name = table_match.group(1)

                        # Rechercher toutes les annotations @Column dans le fichier
                        column_matches = column_pattern.findall(content)
                        for column_name in column_matches:
                            if column_name not in mappings:
                                mappings[column_name] = []
                            mappings[column_name].append((table_name, file_path))
    return mappings

# Rechercher les utilisations des colonnes dans les fichiers Java et capturer les numéros de ligne avec contexte
def search_column_usages(mappings, table_column_names):
    results = []
    for column_name, table_files in mappings.items():
        for table_name, file_path in table_files:
            # Vérifier si cette colonne et cette table sont dans le fichier CSV
            if (table_name, column_name) in table_column_names:
                with open(file_path, 'r', encoding='utf-8') as file:
                    lines = file.readlines()
                    for i, line in enumerate(lines):
                        # Cherche la colonne dans toutes ses formes possibles
                        if re.search(r'\b' + re.escape(column_name) + r'\b', line):
                            # Déterminer le contexte d'utilisation
                            if re.search(r'\bget' + re.escape(column_name.capitalize()) + r'\b', line):
                                context = "getter"
                            elif re.search(r'\bset' + re.escape(column_name.capitalize()) + r'\b', line):
                                context = "setter"
                            else:
                                context = "direct usage"

                            results.append({
                                "column_name": column_name,
                                "table_name": table_name,
                                "file_path": file_path,
                                "line_number": i + 1,
                                "context": context
                            })
    return results

# Charger les noms des tables et colonnes depuis le CSV
table_column_names = load_table_column_names_from_excel(excel_file_path)

# Trouver les mappings des colonnes et tables dans le projet Java
mappings = find_table_column_mappings(project_root)

# Rechercher les apparitions des colonnes et collecter les résultats
results = search_column_usages(mappings, table_column_names)

# Afficher les résultats
print("Column Name | Table Name | File Path | Line Number | Context")
print("-----------------------------------------------------------------")
for result in results:
    print(f"{result['column_name']} | {result['table_name']} | {result['file_path']} | {result['line_number']} | {result['context']}")