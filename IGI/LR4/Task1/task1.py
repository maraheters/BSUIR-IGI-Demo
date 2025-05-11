from .student import Student
from .file_operations import CSVSerializer, PickleSerializer
from .ui import UserInterface

class StudentManager:
    """Class for managing student data and operations."""
    
    def __init__(self):
        """Initialize the student manager with an empty list of students."""
        self.students = []

    def input_custom(self, prompt) -> str:
        filename = input(prompt)
        filename = f"Task1/{filename}"
        return filename

    def add_student(self) -> None:
        """Add a new student to the list."""
        name = UserInterface.get_valid_name()
        birth_date = UserInterface.get_valid_date()
        self.students.append(Student(name, birth_date))
        print("Student added successfully!")
    
    def save_to_csv(self) -> None:
        """Save students to CSV file."""
        if not self.students:
            print("No students to save.")
            return
        
        filename = self.input_custom("Enter CSV filename: ")

        try:
            CSVSerializer.save_students(self.students, filename)
            print(f"Students saved to {filename} successfully!")
        except Exception as e:
            print(f"Error saving to CSV: {e}")
    
    def save_to_pickle(self) -> None:
        """Save students to pickle file."""
        if not self.students:
            print("No students to save.")
            return
        
        filename = self.input_custom("Enter pickle filename: ")
        try:
            PickleSerializer.save_students(self.students, filename)
            print(f"Students saved to {filename} successfully!")
        except Exception as e:
            print(f"Error saving to pickle: {e}")
    
    def load_from_csv(self) -> None:
        """Load students from CSV file."""
        filename = self.input_custom("Enter CSV filename: ")
        try:
            self.students = CSVSerializer.load_students(filename)
            print(f"Students loaded from {filename} successfully!")
        except Exception as e:
            print(f"Error loading from CSV: {e}")
    
    def load_from_pickle(self) -> None:
        """Load students from pickle file."""
        filename = self.input_custom("Enter pickle filename: ")
        try:
            self.students = PickleSerializer.load_students(filename)
            print(f"Students loaded from {filename} successfully!")
        except Exception as e:
            print(f"Error loading from pickle: {e}")
    
    def find_by_month(self) -> None:
        """Find students born in a specific month."""
        if not self.students:
            print("No students in the database.")
            return
        
        month = UserInterface.get_valid_month()
        filtered_students = [s for s in self.students if s.birth_month == month]
        UserInterface.display_students(filtered_students)
    
    def display_all(self) -> None:
        """Display all students."""
        if not self.students:
            print("No students in the database.")
            return
        
        UserInterface.display_students(self.students)

def run_task1():
    """Run Task 1: Student Management System."""
    manager = StudentManager()
    
    while True:
        UserInterface.display_menu()
        choice = UserInterface.get_menu_choice()
        
        if choice == 0:
            print("Returning to main menu...")
            break
        elif choice == 1:
            manager.add_student()
        elif choice == 2:
            manager.save_to_csv()
        elif choice == 3:
            manager.save_to_pickle()
        elif choice == 4:
            manager.load_from_csv()
        elif choice == 5:
            manager.load_from_pickle()
        elif choice == 6:
            manager.find_by_month()
        elif choice == 7:
            manager.display_all() 