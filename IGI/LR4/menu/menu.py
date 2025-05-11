"""
Menu module for task selection and execution.
Lab Work #4: File Operations, Classes, Serializers, and Regular Expressions
"""

from typing import Callable, Dict

class Menu:
    """Class for managing task selection and execution."""
    
    def __init__(self):
        """Initialize the menu with an empty task dictionary."""
        self.tasks: Dict[int, Callable] = {}
    
    def add_task(self, task_number: int, task_function: Callable) -> None:
        """
        Add a task to the menu.
        
        Args:
            task_number (int): Task number
            task_function (Callable): Function to execute for this task
        """
        self.tasks[task_number] = task_function
    
    def display_menu(self) -> None:
        """Display the available tasks menu."""
        print("\n=== Lab Work #4 Menu ===")
        for task_number in sorted(self.tasks.keys()):
            print(f"{task_number}. Task {task_number}")
        print("0. Exit")
    
    def get_choice(self) -> int:
        """
        Get a valid menu choice from user input.
        
        Returns:
            int: Valid menu choice
        """
        while True:
            try:
                choice = int(input("\nEnter your choice: "))
                if choice in self.tasks or choice == 0:
                    return choice
                print(f"Please enter a number between 0 and {max(self.tasks.keys())}.")
            except ValueError:
                print("Please enter a valid number.")
    
    def run(self) -> None:
        """Run the menu system."""
        while True:
            self.display_menu()
            choice = self.get_choice()
            
            if choice == 0:
                print("Goodbye!")
                break
            
            try:
                self.tasks[choice]()
            except Exception as e:
                print(f"Error executing task {choice}: {e}") 