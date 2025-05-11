from .taylor_ln1mx import taylor_ln1mx_partial_sums
import math
import matplotlib.pyplot as plt

def run_task3():
    print("Task 3: График F(x) от количества членов ряда n\n")
    x = float(input("Введите x (|x| < 1): "))
    eps = float(input("Введите eps (точность): "))
    if not (-1 < x < 1):
        print("Ошибка: |x| должен быть < 1")
        return
    
    partial_sums = taylor_ln1mx_partial_sums(x, eps)
    n_vals = list(range(1, len(partial_sums)+1))
    math_val = math.log(1-x)
    
    print(f"math.log(1-x) = {math_val}")
    print(f"Максимальное n: {len(partial_sums)} (eps достигнута)")

    # График
    plt.figure(figsize=(8, 5))
    plt.plot(n_vals, partial_sums, marker='o', label='F(x) — сумма ряда')
    plt.axhline(math_val, color='red', linestyle='--', label='math.log(1-x)')
    plt.xlabel('n (количество членов ряда)')
    plt.ylabel('F(x)')
    plt.title(f'Сходимость ряда для ln(1-x), x={x}, eps={eps}')
    plt.legend()
    plt.grid(True)
    filename = 'Task3/ln1mx_vs_n.png'
    plt.savefig(filename)
    print(f"График сохранён в файл: {filename}")

if __name__ == "__main__":
    run_task3() 