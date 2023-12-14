import csv
import matplotlib.pyplot as plt

# Read the running times from the CSV file
def read_running_times(file_path):
    with open(file_path, 'r') as file:
        reader = csv.reader(file)
        next(reader)  # Skip header if present
        return [(float(row[0]), float(row[1])) for row in reader]

# File path to the CSV file containing running times
csv_file_path = 'runtime.csv'  # Replace with your actual file path

# Read running times
running_times = read_running_times(csv_file_path)

# Unpack data for plotting
input_sizes, actual_running_times = zip(*running_times)

# Plotting the results
plt.plot(input_sizes, actual_running_times, marker='o', label='Actual Running Time')
plt.xlabel('Input Size')
plt.ylabel('Running Time (s)')
plt.title('Genetic Algorithm Running Time vs Input Size')
plt.grid(True)
plt.legend()
plt.show()
