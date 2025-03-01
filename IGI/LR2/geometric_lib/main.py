import square, circle

square_side = float(input("Enter the length of the side of the square: "))
radius = float(input("Enther the radius of the circle: "))

print(f"Area of the square: {square.area(square_side)}")
print(f"Perimeter of the square: {square.perimeter(square_side)}")

print(f"Area of the circle: {circle.area(radius)}")
print(f"Perimeter of the circle: {circle.perimeter(radius)}")
