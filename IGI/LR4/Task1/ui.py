import re
from typing import List, Optional
from .student import Student

class UserInterface:
    """Class for handling user interface and input validation."""
    
    @staticmethod
    def get_valid_month() -> int:
        """
        Get a valid month number from user input.
        
        Returns:
            int: Valid month number (1-12)
        """
        while True:
            try:
                month = int(input("Enter month number (1-12): "))
                if 1 <= month <= 12:
                    return month
                print("Month must be between 1 and 12. Please try again.")
            except ValueError:
                print("Please enter a valid number.")
    
    @staticmethod
    def get_valid_date() -> str:
        """
        Get a valid date in YYYY-MM-DD format from user input.
        
        Returns:
            str: Valid date string
        """
        date_pattern = re.compile(r'^\d{4}-\d{2}-\d{2}$')
        while True:
            date = input("Enter date in YYYY-MM-DD format: ")
            if date_pattern.match(date):
                try:
                    # Validate if it's a real date
                    from datetime import datetime
                    datetime.strptime(date, '%Y-%m-%d')
                    return date
                except ValueError:
                    print("Invalid date. Please try again.")
            else:
                print("Date must be in YYYY-MM-DD format. Please try again.")
    
    @staticmethod
    def get_valid_name() -> str:
        """
        Get a valid student name from user input.
        
        Returns:
            str: Valid student name
        """
        while True:
            name = input("Enter student's full name with initials: ").strip()
            if name and len(name.split()) >= 2:
                return name
            print("Name must contain at least first name and last name. Please try again.")
    
    @staticmethod
    def display_students(students: List[Student]) -> None:
        """
        Display list of students, optionally filtered by birth month.
        
        Args:
            students (List[Student]): List of Student objects
        """
        if not students:
            print("No students found.")
        else:
            for student in students:
                print(student)
    
    @staticmethod
    def display_menu() -> None:
        """Display the main menu options."""
        print("\n=== Student Management System ===")
        print("1. Add new student")
        print("2. Save students to CSV")
        print("3. Save students to Pickle")
        print("4. Load students from CSV")
        print("5. Load students from Pickle")
        print("6. Find students by birth month")
        print("7. Display all students")
        print("0. Exit")
    
    @staticmethod
    def get_menu_choice() -> int:
        """
        Get a valid menu choice from user input.
        
        Returns:
            int: Valid menu choice
        """
        while True:
            try:
                choice = int(input("\nEnter your choice (0-7): "))
                if 0 <= choice <= 7:
                    return choice
                print("Please enter a number between 0 and 7.")
            except ValueError:
                print("Please enter a valid number.") 