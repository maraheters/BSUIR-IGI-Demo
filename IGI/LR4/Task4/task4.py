from .regular_polygon import RegularPolygon

def get_int(prompt, min_value):
    """Prompt the user for an integer >= min_value."""
    while True:
        try:
            value = int(input(prompt))
            if value >= min_value:
                return value
            print(f"Value must be at least {min_value}")
        except ValueError:
            print("Please enter an integer.")

def get_float(prompt, min_value):
    """Prompt the user for a float > min_value."""
    while True:
        try:
            value = float(input(prompt))
            if value > min_value:
                return value
            print(f"Value must be greater than {min_value}")
        except ValueError:
            print("Please enter a number.")

def run_task4():
    """Run Task 4: Create, display, and save a regular polygon with user parameters."""
    print("Drawing a regular n-gon\n")
    n = get_int("Enter the number of sides n (n >= 3): ", 3)
    a = get_float("Enter the side length a (> 0): ", 0)
    color = input("Enter the color of the figure (e.g., 'blue', 'red', 'green'): ")
    label = input("Enter a label for the figure: ")

    polygon = RegularPolygon(n, a, color)
    print(polygon.info())
    polygon.plot(label, filename='Task4/regular_polygon.png')
    print("Figure saved to file Task4/regular_polygon.png")

if __name__ == '__main__':
    run_task4() 