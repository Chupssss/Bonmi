import sqlite3

# Подключение к существующей базе данных project_new
conn = sqlite3.connect("project_new.db")  # Укажите точный путь к вашей базе, если она не в текущей папке
cursor = conn.cursor()


products = []
with open('product.txt', "r", encoding="utf-8") as file:
    for line in file:
        product_name = line.strip()
        if product_name:
            products.append((product_name,))



def insert_products(products):
    cursor.executemany('''
        INSERT INTO product (name) VALUES (?)
    ''', products)
    conn.commit()


insert_products(sorted(products))
conn.close()
