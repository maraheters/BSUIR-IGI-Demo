from abc import ABC, abstractmethod
import math
import matplotlib.pyplot as plt

class GeometricFigure(ABC):
    """Abstract base class for geometric figures."""
    
    def __init__(self, color):
        """
        Initialize a geometric figure with a color.
        Args:
            color (str): Color name
        """
        self.color = FigureColor(color)

    @abstractmethod
    def area(self):
        """Calculate the area of the figure."""
        pass

class FigureColor:
    """Class for storing the color of a geometric figure."""
    def __init__(self, color):
        """Initialize with a color string."""
        self._color = color

    @property
    def color(self):
        """Get the color of the figure."""
        return self._color

    @color.setter
    def color(self, value):
        """Set the color of the figure."""
        self._color = value

class RegularPolygon(GeometricFigure):
    """Class representing a regular n-sided polygon."""
    figure_name = "Regular n-gon"

    def __init__(self, n, a, color):
        """
        Initialize a regular polygon.
        Args:
            n (int): Number of sides
            a (float): Side length
            color (str): Color name
        """
        
        super().__init__(color)
        self.n = n
        self.a = a

    def area(self):
        """Calculate the area of the regular polygon."""
        # Formula for the area of a regular n-sided polygon with side length a
        return (self.n * self.a ** 2) / (4 * math.tan(math.pi / self.n))

    def info(self):
        """Return a string with the main parameters, color, and area of the polygon."""
        return "Figure: {0}, n: {1}, side: {2}, color: {3}, area: {4:.2f}".format(
            self.figure_name, self.n, self.a, self.color.color, self.area()
        )

    @classmethod
    def name(cls):
        """Return the name of the figure."""
        return cls.figure_name

    def plot(self, label, filename=None):
        """
        Draw and fill the polygon with the specified color and label.
        Args:
            label (str): Text label for the figure
            filename (str, optional): If provided, save the figure to this file
        """
        # Calculate vertex coordinates
        angles = [2 * math.pi * i / self.n for i in range(self.n)]
        x = [self.a * math.cos(angle) for angle in angles]
        y = [self.a * math.sin(angle) for angle in angles]
        x.append(x[0])
        y.append(y[0])
        plt.figure(figsize=(6, 6))
        plt.fill(x, y, color=self.color.color, alpha=0.5)
        plt.plot(x, y, color='black')
        plt.title(label)
        plt.axis('equal')
        plt.grid(True)
        plt.annotate(label, xy=(0, 0), ha='center', fontsize=12, color='black')
        if filename:
            plt.savefig(filename)
        plt.show()