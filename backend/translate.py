import csv
from googletrans import Translator

# Создание переводчика
translator = Translator()

# Чтение CSV и перевод данных

with open('input.csv', 'r', encoding='utf-8') as csv_file:
    csv_reader = csv.reader(csv_file)
    headers = next(csv_reader)  # Чтение заголовков
    translated_lines = []

    # Перевод строк
    for row in csv_reader:
        translated_row = [translator.translate(cell, src='en', dest='ru').text for cell in row]
        print(1)
        translated_lines.append(", ".join(translated_row))


# Запись перевода в TXT-файл
with open('output.txt', 'w', encoding='utf-8') as txt_file:
    for line in translated_lines:
        txt_file.write(line + "\n")
