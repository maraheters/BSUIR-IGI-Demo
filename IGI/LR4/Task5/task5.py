import numpy as np


class NumPyMatrixDemo:
    """
    Class to demonstrate NumPy array creation, indexing, operations, and statistics.
    Also solves the individual assignment for the matrix.
    """

    def __init__(self):
        """Initialize the class."""
        self.matrix = None

    def _create_matrix(self, n, m):
        """
        Create an integer matrix of shape (n, m) with random values from 0 to 20.
        Args:
            n (int): Number of rows
            m (int): Number of columns
        Returns:
            np.ndarray: Random integer matrix
        """
        return np.random.randint(0, 21, size=(n, m))

    def _get_positive_int(self, prompt):
        """Prompt the user for a positive integer."""
        while True:
            try:
                value = int(input(prompt))
                if value > 0:
                    return value
                print("Value must be a positive integer.")
            except ValueError:
                print("Please enter a valid integer.")

    def _execute_task(self):
        """Execute the main functionality of the task."""
        print("NumPy Matrix Demo (Task 5)")
        n = self._get_positive_int("Enter the number of rows (n > 0): ")
        m = self._get_positive_int("Enter the number of columns (m > 0): ")
        self.matrix = self._create_matrix(n, m)
        print("\nMatrix A:")
        print(self.matrix)

        # 1. Array creation: array() and values
        arr1 = np.array([1, 2, 3])
        print("\nArray created with np.array():", arr1)
        arr2 = np.arange(5)
        print("Array created with np.arange():", arr2)

        # 2. Array creation functions
        zeros = np.zeros((2, 3), dtype=int)
        ones = np.ones((2, 3), dtype=int)
        eye = np.eye(3, dtype=int)
        print("\nZeros array:\n", zeros)
        print("Ones array:\n", ones)
        print("Identity matrix:\n", eye)

        # 3. Indexing and slicing
        print("\nA[0, 0] (first element):", self.matrix[0, 0])
        print("A[0, :] (first row):", self.matrix[0, :])
        print("A[:, 0] (first column):", self.matrix[:, 0])
        print("A[1:3, 1:3] (submatrix):\n", self.matrix[1:3, 1:3])

        # 4. Universal functions
        print("\nA + 10:\n", self.matrix + 10)
        print("A squared:\n", self.matrix ** 2)
        print("A transposed:\n", self.matrix.T)

        # Math/statistics
        print("\nMean of A:", np.mean(self.matrix))
        print("Median of A:", np.median(self.matrix))
        print("Correlation coefficient of A (flattened):\n", np.corrcoef(self.matrix.flatten()))
        print("Variance of A:", np.var(self.matrix))
        print("Standard deviation of A:", np.std(self.matrix))

        # Individual assignment
        # Sum below main diagonal
        below_diag_sum = np.sum(np.tril(self.matrix, k=-1))
        print("\nSum of elements below the main diagonal:", below_diag_sum)

        # Standard deviation of main diagonal (two ways)
        diag = np.diag(self.matrix)
        std_builtin = np.std(diag)
        std_formula = (sum((x - np.mean(diag)) ** 2 for x in diag) / len(diag)) ** 0.5
        print("Standard deviation of main diagonal (builtin): {:.2f}".format(std_builtin))
        print("Standard deviation of main diagonal (formula): {:.2f}".format(std_formula))

    def run(self):
        """Run the entire demonstration."""
        self._execute_task()


# Entry point of the script
def run_task5():
    demo = NumPyMatrixDemo()
    demo.run()