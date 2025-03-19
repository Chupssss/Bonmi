import pandas as pd
import sqlite3
import matplotlib.pyplot as plt
import numpy as np
from datetime import datetime, timedelta
import requests


def download_moex_data():
    end_date = datetime.today().strftime("%Y-%m-%d")
    start_date = (datetime.today() - timedelta(days=5 * 365)).strftime("%Y-%m-%d")
    url = f"https://iss.moex.com/iss/engines/stock/markets/index/boards/SNDX/securities/IMOEX/candles.json?from={start_date}&till={end_date}&interval=24"
    response = requests.get(url, timeout=10)
    response.raise_for_status()
    data = response.json()
    columns = data["candles"]["columns"]
    rows = data["candles"]["data"]
    df = pd.DataFrame(rows, columns=columns)
    df["begin"] = pd.to_datetime(df["begin"])
    df.rename(columns={"begin": "date"}, inplace=True)

    csv_path = 'moex_data.csv'
    df.to_csv(csv_path, index=False)
    return csv_path


def save_to_sqlite(csv_file):
    df = pd.read_csv(csv_file)
    df["date"] = pd.to_datetime(df["date"])
    conn = sqlite3.connect("moex_data")
    df.to_sql("moex", conn, if_exists="replace", index=False)
    conn.close()


def analyze_std_deviation():
    conn = sqlite3.connect("moex_data")
    df = pd.read_sql("SELECT * FROM moex", conn)
    conn.close()
    df["date"] = pd.to_datetime(df["date"])
    df["year"] = df["date"].dt.year
    df["month"] = df["date"].dt.month
    monthly_avg = df.groupby(["year", "month"])["close"].mean().reset_index()
    df = df.merge(monthly_avg, on=["year", "month"], suffixes=("", "_mean"))
    df["deviation"] = abs(df["close"] - df["close_mean"])
    monthly_deviation = df.groupby(["year", "month"])["deviation"].mean().reset_index()
    monthly_deviation = monthly_deviation.round(2)
    filtered = monthly_deviation[monthly_deviation["deviation"] > 100]
    plt.figure(figsize=(12, 6))
    plt.plot(monthly_deviation.index, monthly_deviation["deviation"], marker="o", linestyle="-")
    plt.xlabel("Дата (Месяцы)")
    plt.ylabel("Среднее отклонение")
    plt.title("Среднее отклонение цен закрытия по месяцам")
    plt.grid()
    plt.show()
    return filtered


def detect_breakouts():
    conn = sqlite3.connect("moex_data")
    df = pd.read_sql("SELECT * FROM moex", conn)
    conn.close()
    df["date"] = pd.to_datetime(df["date"])
    df["year"] = df["date"].dt.year
    df["month"] = df["date"].dt.month
    monthly_minmax = df.groupby(["year", "month"])["close"].agg(["min", "max"]).reset_index()
    df = df.merge(monthly_minmax, on=["year", "month"])
    df["change"] = df["close"].diff().abs()
    df["threshold"] = 0.2 * (df["max"] - df["min"])
    df["breakout"] = df["change"] >= df["threshold"]
    df["prev_close"] = df["close"].shift(1)
    df["prev_prev_close"] = df["close"].shift(2)
    conditions = [
        (df["prev_prev_close"] >= df["prev_close"]) & (df["prev_close"] > df["close"]),
        (df["prev_prev_close"] <= df["prev_close"]) & (df["prev_close"] > df["close"]),
        (df["prev_prev_close"] >= df["prev_close"]) & (df["prev_close"] < df["close"]),
        (df["prev_prev_close"] <= df["prev_close"]) & (df["prev_close"] < df["close"]),
    ]
    values = ["boost down", "local max", "local min", "boost up"]
    df["breakout_type"] = np.select(conditions, values, default="")
    breakout_points = df[df["breakout"]]
    result = breakout_points[["year", "month", "date", "close", "max", "min", "breakout_type"]]

    return result


csv_file = download_moex_data()
filtered_deviations = analyze_std_deviation()
breakout_points = detect_breakouts()
print("Отклонения > 100 единиц:")
print(filtered_deviations.to_string(index=False))

print("\n  Пробои:")
print(breakout_points.to_string(index=False))



