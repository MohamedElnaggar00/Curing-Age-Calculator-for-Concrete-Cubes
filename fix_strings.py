import os

file_path = "app/src/main/res/values/strings.xml"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace('>7 Days<', '>&#x200E;7 Days<')
content = content.replace('>14 Days<', '>&#x200E;14 Days<')
content = content.replace('>28 Days<', '>&#x200E;28 Days<')
content = content.replace('>56 Days<', '>&#x200E;56 Days<')
# Ensure Breaking Date -> Testing Date is applied
content = content.replace('Breaking Date', 'Testing Date')
content = content.replace('breaking_date', 'testing_date') 

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

file_path_ar = "app/src/main/res/values-ar/strings.xml"
with open(file_path_ar, "r", encoding="utf-8") as f:
    content_ar = f.read()

content_ar = content_ar.replace('breaking_date', 'testing_date') 
with open(file_path_ar, "w", encoding="utf-8") as f:
    f.write(content_ar)
