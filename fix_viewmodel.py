import os

file_path = "app/src/main/java/com/example/ui/MainViewModel.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Replace the Arabic defaults
content = content.replace('val proj = _projectName.value.ifBlank { "مشروع جديد" }', 'val proj = _projectName.value')
content = content.replace('val elem = _elementName.value.ifBlank { "عينة صب" }', 'val elem = _elementName.value')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)
