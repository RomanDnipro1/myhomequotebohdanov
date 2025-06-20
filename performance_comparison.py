import matplotlib.pyplot as plt
import numpy as np
import math

# Налаштування для української мови
plt.rcParams['font.family'] = 'DejaVu Sans'
plt.rcParams['axes.unicode_minus'] = False

# Дані для графіків
n_values = np.array([10, 50, 100, 500, 1000, 5000, 10000])
k_values = np.array([5, 10, 20, 50])

# Функції складності
def getTopResultsByUser_complexity(n, k):
    return k  # O(k)

def getTopResultsByLevel_complexity(n, k):
    return n * math.log2(n) + k  # O(n log n + k)

def setResult_user_complexity(n):
    return n  # O(n)

def setResult_level_complexity(n):
    return 1  # O(1)

# Створення графіків
fig, ((ax1, ax2), (ax3, ax4)) = plt.subplots(2, 2, figsize=(15, 12))
fig.suptitle('Порівняння продуктивності методів UserResultService', fontsize=16, fontweight='bold')

# Графік 1: Читання - getTopResultsByUser vs getTopResultsByLevel
ax1.set_title('Читання: getTopResultsByUser vs getTopResultsByLevel', fontweight='bold')
ax1.set_xlabel('Кількість елементів (n)')
ax1.set_ylabel('Складність (операції)')
ax1.grid(True, alpha=0.3)

for k in k_values:
    user_complexity = [getTopResultsByUser_complexity(n, k) for n in n_values]
    level_complexity = [getTopResultsByLevel_complexity(n, k) for n in n_values]
    
    ax1.plot(n_values, user_complexity, 'o-', label=f'getTopResultsByUser (k={k})', linewidth=2)
    ax1.plot(n_values, level_complexity, 's-', label=f'getTopResultsByLevel (k={k})', linewidth=2)

ax1.legend()
ax1.set_xscale('log')
ax1.set_yscale('log')

# Графік 2: Вставка - setResult для user vs level
ax2.set_title('Вставка: setResult для user vs level', fontweight='bold')
ax2.set_xlabel('Кількість елементів (n)')
ax2.set_ylabel('Складність (операції)')
ax2.grid(True, alpha=0.3)

user_insert = [setResult_user_complexity(n) for n in n_values]
level_insert = [setResult_level_complexity(n) for n in n_values]

ax2.plot(n_values, user_insert, 'o-', label='setResult (user)', linewidth=2, color='green')
ax2.plot(n_values, level_insert, 's-', label='setResult (level)', linewidth=2, color='red')

ax2.legend()
ax2.set_xscale('log')
ax2.set_yscale('log')

# Графік 3: Порівняння загальної продуктивності
ax3.set_title('Загальна продуктивність: Вставка + Читання', fontweight='bold')
ax3.set_xlabel('Кількість елементів (n)')
ax3.set_ylabel('Складність (операції)')
ax3.grid(True, alpha=0.3)

# Припустимо, що k = 20 (DEFAULT_TOP_SIZE)
k = 20
user_total = [setResult_user_complexity(n) + getTopResultsByUser_complexity(n, k) for n in n_values]
level_total = [setResult_level_complexity(n) + getTopResultsByLevel_complexity(n, k) for n in n_values]

ax3.plot(n_values, user_total, 'o-', label='User підхід (O(n) + O(k))', linewidth=2, color='blue')
ax3.plot(n_values, level_total, 's-', label='Level підхід (O(1) + O(n log n + k))', linewidth=2, color='orange')

ax3.legend()
ax3.set_xscale('log')
ax3.set_yscale('log')

# Графік 4: Відношення продуктивності
ax4.set_title('Відношення продуктивності: Level/User', fontweight='bold')
ax4.set_xlabel('Кількість елементів (n)')
ax4.set_ylabel('Відношення складності (Level/User)')
ax4.grid(True, alpha=0.3)

ratio = np.array(level_total) / np.array(user_total)
ax4.plot(n_values, ratio, 'o-', linewidth=2, color='purple')
ax4.axhline(y=1, color='red', linestyle='--', alpha=0.7, label='Рівень рівності')

ax4.legend()
ax4.set_xscale('log')

plt.tight_layout()
plt.savefig('performance_comparison.png', dpi=300, bbox_inches='tight')
plt.show()

# Додатковий графік: Детальний аналіз getTopResultsByLevel
fig2, (ax5, ax6) = plt.subplots(1, 2, figsize=(15, 6))
fig2.suptitle('Детальний аналіз getTopResultsByLevel', fontsize=16, fontweight='bold')

# Графік 5: Розбір складових getTopResultsByLevel
ax5.set_title('Складові getTopResultsByLevel: O(n log n + k)', fontweight='bold')
ax5.set_xlabel('Кількість елементів (n)')
ax5.set_ylabel('Складність (операції)')
ax5.grid(True, alpha=0.3)

sorting_complexity = [n * math.log2(n) for n in n_values]
limit_complexity = [k for n in n_values]
total_complexity = [n * math.log2(n) + k for n in n_values]

ax5.plot(n_values, sorting_complexity, 'o-', label='Сортування O(n log n)', linewidth=2, color='red')
ax5.plot(n_values, limit_complexity, 's-', label='Обмеження O(k)', linewidth=2, color='green')
ax5.plot(n_values, total_complexity, '^-', label='Загальна O(n log n + k)', linewidth=2, color='blue')

ax5.legend()
ax5.set_xscale('log')
ax5.set_yscale('log')

# Графік 6: Порівняння з getTopResultsByUser
ax6.set_title('Порівняння: getTopResultsByUser vs getTopResultsByLevel', fontweight='bold')
ax6.set_xlabel('Кількість елементів (n)')
ax6.set_ylabel('Складність (операції)')
ax6.grid(True, alpha=0.3)

user_read = [getTopResultsByUser_complexity(n, k) for n in n_values]
level_read = [getTopResultsByLevel_complexity(n, k) for n in n_values]

ax6.plot(n_values, user_read, 'o-', label='getTopResultsByUser O(k)', linewidth=2, color='green')
ax6.plot(n_values, level_read, 's-', label='getTopResultsByLevel O(n log n + k)', linewidth=2, color='red')

ax6.legend()
ax6.set_xscale('log')
ax6.set_yscale('log')

plt.tight_layout()
plt.savefig('detailed_analysis.png', dpi=300, bbox_inches='tight')
plt.show()

print("Графіки збережено як 'performance_comparison.png' та 'detailed_analysis.png'")
print("\nВисновки:")
print("1. getTopResultsByUser має константну складність O(k) для читання")
print("2. getTopResultsByLevel має логарифмічну складність O(n log n + k) для читання")
print("3. При великих n, getTopResultsByLevel стає значно повільнішим")
print("4. getTopResultsByUser має O(n) для вставки, але O(k) для читання")
print("5. getTopResultsByLevel має O(1) для вставки, але O(n log n + k) для читання") 