import os

files = [
    "app/src/main/java/com/example/MainActivity.kt",
    "app/src/main/java/com/example/ui/screens/ConcreteCalcScreen.kt",
    "app/src/main/java/com/example/ui/screens/DateDiffScreen.kt",
    "app/src/main/java/com/example/ui/screens/SavedBatchesScreen.kt"
]

for f in files:
    with open(f, 'r') as file:
        lines = file.readlines()
    
    # Remove all stringResource and R imports
    lines = [l for l in lines if l.strip() not in ['import com.example.R', 'import androidx.compose.ui.res.stringResource']]
    
    # Find package line
    for i, l in enumerate(lines):
        if l.startswith('package '):
            lines.insert(i + 1, '\nimport com.example.R\nimport androidx.compose.ui.res.stringResource\n')
            break
            
    with open(f, 'w') as file:
        file.writelines(lines)
