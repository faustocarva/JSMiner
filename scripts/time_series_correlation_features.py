import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from statsmodels.nonparametric.smoothers_lowess import lowess

df = pd.read_csv('~/Documents/JSMiner/scripts/results-without-gaps.csv')

df = df.drop(columns=['revision', 'errors'])
df['date'] = pd.to_datetime(df['date'], format='%Y-%m-%d')

df['year_month'] = df['date'].dt.strftime('%Y-%m')

# Group DataFrame by project and year/month
grouped = df.groupby(['project', 'year_month'])

# Find the index of the last revision in each group
last_revision_idx = grouped['date'].idxmax()

df_last_revision = df.loc[last_revision_idx]

# Define variables of interest
id_vars = ["project", "date", "statements", "files", "year_month"]
value_name = "total"
var_name = "feature"

# Melt the DataFrame into the appropriate format
melted_df = pd.melt(df_last_revision, id_vars=id_vars, value_name=value_name, var_name=var_name)

# Convert the 'date' column to datetime
melted_df['date'] = pd.to_datetime(melted_df['date'], format='%Y-%m-%d', errors='coerce')

# Convert the 'year_month' column to datetime
melted_df['year_month'] = pd.to_datetime(melted_df['year_month'], format='%Y-%m', errors='coerce')

# Convert the 'total' column to numeric
melted_df['total'] = pd.to_numeric(melted_df['total'], errors='coerce')

melted_df = melted_df.sort_values(by='year_month')

# List of features
features = [
    'async_declarations',
    'await_declarations',
    'const_declarations',
    'class_declarations',
    'arrow_function_declarations',
    'let_declarations',
    'export_declarations',
    'import_statements',
    'promise_declarations',
    'promise_all_and_then',
    'default_parameters',
    'rest_statements',
    'spread_arguments',
    'array_destructuring',
    'object_destructuring',
    # 'yield_declarations',
    'optional_chain',
    'template_string_expressions',
    # 'null_coalesce_operators',
    # 'hashbang_comments',
    # 'private_fields',
    # 'numeric_separator',
]

# Calculate the correlation among selected features, files, and statements
correlation_df = melted_df.pivot_table(index='year_month', columns='feature', values='total', aggfunc='sum')
correlation_df = correlation_df[features]  # Select only the features from the list
correlation_df['files'] = melted_df.groupby('year_month')['files'].mean()
correlation_df['statements'] = melted_df.groupby('year_month')['statements'].mean()
correlation_matrix = correlation_df.corr()

# Scale down the numbers in the correlation matrix
correlation_matrix = correlation_matrix.round(2)
# correlation_matrix_filtered = correlation_matrix[correlation_matrix > 0.6]

features_mapping = {
    'async_declarations': 'Async Declarations',
    'await_declarations': 'Await Declarations',
    'const_declarations': 'Const Declarations',
    'arrow_function_declarations': 'Arrow Function Declarations',
    'let_declarations': 'Let Declarations',
    'export_declarations': 'Export Declarations',
    'import_statements': 'Import Statements',
    'class_declarations': 'Class Declarations',
    'default_parameters': 'Default Parameters',
    'rest_statements': 'Rest Statements',
    'array_destructuring': 'Array Destructuring',
    'promise_declarations': 'Promise Declarations',
    'promise_all_and_then': 'Promise All() and Then()',
    'spread_arguments': 'Spread Arguments',
    'object_destructuring': 'Object Destructuring',
    'yield_declarations': 'Yield Declarations',
    'optional_chain': 'Optional Chain',
    'template_string_expressions': 'Template String Expressions',
    'null_coalesce_operators': 'Null Coalesce Operators',
    'hashbang_comments': 'Hashbang Comments',
    'exponentiation_assignments': 'Exponentiation Assignments',
    'private_fields': 'Private Fields',
    'numeric_separator': 'Numeric Separator',
    'files': 'Files',
    'statements': 'Statements',
}

feature_names = [features_mapping[feature] for feature in correlation_matrix.columns]
# Plot the correlation matrix
plt.figure(figsize=(12, 10))
heatmap = sns.heatmap(correlation_matrix, annot=True, cmap='coolwarm', fmt=".2f", cbar=True, square=True, annot_kws={"size": 10})
plt.title('Correlation Matrix')

# Mapeando os nomes das features para os y-labels
feature_names_y = [features_mapping[feature] for feature in correlation_matrix.index if feature in features_mapping]

heatmap.set_xticklabels(feature_names, rotation=45, ha='right')
heatmap.set_yticklabels(feature_names)

# Adicionando margens e centralizando a figura
plt.subplots_adjust(left=0.05, right=0.95, top=0.95, bottom=0.05)

# Ajustando o retângulo de layout para aumentar o tamanho dentro do PDF
plt.tight_layout(rect=[0.05, 0.05, 0.95, 0.95])

# Salvar a tabela de correlação em LaTeX
correlation_latex = correlation_matrix.to_latex(float_format="%.2f")
with open("correlation_matrix.tex", "w") as text_file:
    text_file.write(correlation_latex)

# Salvar o gráfico em PDF
pdf_filename = os.path.join(f'features_correlation.pdf')
plt.savefig(pdf_filename, format='pdf')