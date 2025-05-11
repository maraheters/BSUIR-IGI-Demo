import csv
import pickle
from typing import List
from .student import Student

class FileHandlerMixin:
    """Mixin class for handling file operations."""
    
    @staticmethod
    def _open_file(filename: str, mode: str, binary: bool = False):
        """
        Open a file with the specified mode.
        
        Args:
            filename (str): Name of the file
            mode (str): File mode ('r', 'w', etc.)
            binary (bool): Whether to open in binary mode
        
        Returns:
            file object
        """
        return open(filename, mode + ('b' if binary else ''))

class StudentSerializer:
    """Base class for student serialization operations."""
    
    @staticmethod
    def students_to_dict(students: List[Student]) -> List[dict]:
        """Convert list of Student objects to list of dictionaries."""
        return [
            {
                'full_name': student.full_name,
                'birth_date': student.birth_date.strftime('%Y-%m-%d')
            }
            for student in students
        ]
    
    @staticmethod
    def dict_to_students(data: List[dict]) -> List[Student]:
        """Convert list of dictionaries to list of Student objects."""
        return [
            Student(student['full_name'], student['birth_date'])
            for student in data
        ]

class CSVSerializer(FileHandlerMixin, StudentSerializer):
    """Class for handling CSV serialization of student data."""
    
    @staticmethod
    def save_students(students: List[Student], filename: str) -> None:
        """
        Save students to CSV file.
        
        Args:
            students (List[Student]): List of Student objects
            filename (str): Name of the CSV file
        """
        with CSVSerializer._open_file(filename, 'w') as file:
            writer = csv.DictWriter(file, fieldnames=['full_name', 'birth_date'])
            writer.writeheader()
            writer.writerows(StudentSerializer.students_to_dict(students))
    
    @staticmethod
    def load_students(filename: str) -> List[Student]:
        """
        Load students from CSV file.
        
        Args:
            filename (str): Name of the CSV file
            
        Returns:
            List[Student]: List of Student objects
        """
        with CSVSerializer._open_file(filename, 'r') as file:
            reader = csv.DictReader(file)
            return StudentSerializer.dict_to_students(list(reader))

class PickleSerializer(FileHandlerMixin, StudentSerializer):
    """Class for handling pickle serialization of student data."""
    
    @staticmethod
    def save_students(students: List[Student], filename: str) -> None:
        """
        Save students to pickle file.
        
        Args:
            students (List[Student]): List of Student objects
            filename (str): Name of the pickle file
        """
        with PickleSerializer._open_file(filename, 'w', binary=True) as file:
            pickle.dump(StudentSerializer.students_to_dict(students), file)
    
    @staticmethod
    def load_students(filename: str) -> List[Student]:
        """
        Load students from pickle file.
        
        Args:
            filename (str): Name of the pickle file
            
        Returns:
            List[Student]: List of Student objects
        """
        with PickleSerializer._open_file(filename, 'r', binary=True) as file:
            data = pickle.load(file)
            return StudentSerializer.dict_to_students(data)