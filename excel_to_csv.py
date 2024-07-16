import pandas as pd

def excel_to_csv(input_excel_file, output_csv_file):
    # Charger le fichier Excel dans un DataFrame
    df = pd.read_excel(input_excel_file)
    
    # Écrire le DataFrame dans un fichier CSV
    df.to_csv(output_csv_file, index=False)  # index=False pour ne pas écrire l'index du DataFrame

# Exemple d'utilisation
input_excel_file = 'table.xlsx'  # Remplacez par le chemin de votre fichier Excel
output_csv_file = 'table_csv.csv'   # Nom du fichier CSV de sortie

excel_to_csv(input_excel_file, output_csv_file)