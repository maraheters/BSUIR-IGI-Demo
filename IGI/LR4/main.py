"""
Main program module for Lab Work #4.
Lab Work #4: File Operations, Classes, Serializers, and Regular Expressions
Version: 1.0
Developer: Vladislav Fedotov
Date: 28.04.2025
"""

from menu.menu import Menu
from Task1.task1 import run_task1
from Task2.task2 import run_task2
from Task3.task3 import run_task3
from Task4.task4 import run_task4
from Task5.task5 import run_task5

def main():
    """Main program entry point."""
    menu = Menu()
    
    # Register tasks
    menu.add_task(1, run_task1)
    menu.add_task(2, run_task2)
    menu.add_task(3, run_task3)
    menu.add_task(4, run_task4)
    menu.add_task(5, run_task5)
    
    menu.run()

if __name__ == "__main__":
    main() 