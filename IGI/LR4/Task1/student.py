from datetime import datetime

class Student:
    """
    A class representing a student with personal information.
    
    Attributes:
        full_name (str): Student's full name with initials
        birth_date (datetime): Student's date of birth
        birth_month (int): Student's birth month
        birth_year (int): Student's birth year
    """
    
    def __init__(self, full_name: str, birth_date: str):
        """
        Initialize a new Student instance.
        
        Args:
            full_name (str): Student's full name with initials
            birth_date (str): Date of birth in format 'YYYY-MM-DD'
        """
        self._full_name = full_name
        self._birth_date = datetime.strptime(birth_date, '%Y-%m-%d')
        self._birth_month = self._birth_date.month
        self._birth_year = self._birth_date.year
    
    @property
    def full_name(self) -> str:
        """Get student's full name."""
        return self._full_name
    
    @full_name.setter
    def full_name(self, value: str) -> None:
        """Set student's full name."""
        self._full_name = value
    
    @property
    def birth_date(self) -> datetime:
        """Get student's birth date."""
        return self._birth_date
    
    @birth_date.setter
    def birth_date(self, value: str) -> None:
        """Set student's birth date."""
        self._birth_date = datetime.strptime(value, '%Y-%m-%d')
        self._birth_month = self._birth_date.month
        self._birth_year = self._birth_date.year
    
    @property
    def birth_month(self) -> int:
        """Get student's birth month."""
        return self._birth_month
    
    @property
    def birth_year(self) -> int:
        """Get student's birth year."""
        return self._birth_year
    
    def __str__(self) -> str:
        """Return string representation of the student."""
        return f"{self._full_name}, born {self._birth_date.strftime('%Y-%m-%d')}"
    
    def __repr__(self) -> str:
        """Return official string representation of the student."""
        return f"Student('{self._full_name}', '{self._birth_date.strftime('%Y-%m-%d')}')" 