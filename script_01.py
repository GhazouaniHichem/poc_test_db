import os
import pandas as pd

def find_java_files_with_database_names(root_directory, excel_file):
    # Lire le fichier Excel et extraire la colonne 'database_name'
    df = pd.read_excel(excel_file)
    database_names = df['table_name'].dropna().unique()  # Supprime les valeurs NaN et obtient les valeurs uniques

    matched_files = []

    # Parcourir les répertoires et les fichiers
    for subdir, _, files in os.walk(root_directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(subdir, file)
                
                # Ouvrir et lire chaque fichier .java
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    
                    # Vérifier si le contenu du fichier contient l'une des valeurs de 'database_name'
                    for db_name in database_names:
                        if db_name in content:
                            matched_files.append((file_path, db_name))
                            break
    
    return matched_files




root_directory = 'Java-app'
excel_file = 'table.xlsx'
matched_files = find_java_files_with_database_names(root_directory, excel_file)




for file, db_name in matched_files:
    print(f'Fichier: {file} contient table_name: {db_name}')