import os

files = [
    "app/src/main/java/com/example/ui/screens/ConcreteCalcScreen.kt",
    "app/src/main/java/com/example/ui/screens/DateDiffScreen.kt",
    "app/src/main/java/com/example/ui/screens/SavedBatchesScreen.kt"
]

for f in files:
    with open(f, "r", encoding="utf-8") as file:
        content = file.read()
    
    content = content.replace('R.string.breaking_date', 'R.string.testing_date')
    
    with open(f, "w", encoding="utf-8") as file:
        file.write(content)
