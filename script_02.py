import os
import pandas as pd

def find_java_files_with_database_and_table_names(root_directory, excel_file):
    # Lire le fichier Excel et extraire les colonnes 'database_name' et 'table_name'
    df = pd.read_excel(excel_file)
    
    # Créer une liste de tuples (database_name, table_name) à partir du DataFrame
    db_table_combinations = [(row['table_name'], row['column_name']) for index, row in df.iterrows()]

    matched_files = []

    # Parcourir les répertoires et les fichiers
    for subdir, _, files in os.walk(root_directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(subdir, file)
                
                # Ouvrir et lire chaque fichier .java
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    
                    # Vérifier si le contenu du fichier contient les combinaisons database_name et table_name
                    for db_name, table_name in db_table_combinations:
                        if db_name in content and table_name in content:
                            matched_files.append((file_path, db_name, table_name))
    
    return matched_files







root_directory = 'hiber'
excel_file = 'table.xlsx'
matched_files = find_java_files_with_database_and_table_names(root_directory, excel_file)



for file, db_name, table_name in matched_files:
    print(f'Fichier: {file} contient table_name: {db_name} et column_name: {table_name}')
