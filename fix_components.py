import os

file_path = "app/src/main/java/com/example/ui/components/OneUIComponents.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace('.padding(horizontal = 12.dp, vertical = 8.dp)', '.padding(horizontal = 4.dp, vertical = 8.dp)')
content = content.replace('fontSize = 13.sp', 'fontSize = 12.sp')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)
