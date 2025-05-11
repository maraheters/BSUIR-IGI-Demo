import math
import numpy as np
import matplotlib.pyplot as plt
import statistics


def taylor_ln1mx(x, n_terms):
    """Вычисляет сумму ряда Тейлора для ln(1-x) по n_terms членам."""
    s = 0
    for n in range(1, n_terms+1):
        s -= x**n / n
    return s


def taylor_ln1mx_eps(x, eps):
    """Вычисляет сумму ряда Тейлора для ln(1-x) с точностью eps. Возвращает сумму и количество членов."""
    s = 0
    n = 1
    term = -x
    while abs(term) > eps:
        s += term
        n += 1
        term = -x**n / n
    return s, n-1


def taylor_ln1mx_partial_sums(x, eps):
    """Возвращает список частичных сумм ряда для ln(1-x) для каждого n, пока очередной член по модулю больше eps."""
    sums = []
    s = 0
    n = 1
    term = -x
    while abs(term) > eps:
        s += term
        sums.append(s)
        n += 1
        term = -x**n / n
    return sums


def compute_statistics(data):
    """Вычисляет среднее, медиану, моду, дисперсию и СКО для последовательности."""
    mean = statistics.mean(data)
    median = statistics.median(data)
    try:
        mode = statistics.mode(data)
    except statistics.StatisticsError:
        mode = None
    variance = statistics.variance(data)
    std_dev = statistics.stdev(data)
    return mean, median, mode, variance, std_dev


def plot_ln1mx(x_vals, taylor_vals, math_vals, n_terms, filename):
    """Строит и сохраняет график разложения и точной функции."""
    plt.figure(figsize=(10, 6))
    plt.plot(x_vals, taylor_vals, label=f'Разложение в ряд (n={n_terms})', color='blue')
    plt.plot(x_vals, math_vals, label='math.log(1-x)', color='red', linestyle='--')
    plt.axhline(0, color='black', linewidth=0.5)
    plt.axvline(0, color='black', linewidth=0.5)
    plt.legend()
    plt.xlabel('x')
    plt.ylabel('F(x)')
    plt.title('Сравнение разложения ln(1-x) и math.log(1-x)')
    plt.annotate('Сравнение разложения и точной функции', xy=(0.5, -0.5), xytext=(0.2, -1.5),
                 arrowprops=dict(facecolor='black', shrink=0.05))
    plt.grid(True)
    plt.savefig(filename)
    plt.close() 