import random as rand

def max_in_list(arr):
    value = [sum(2**(4-j) * arr[i][j] for j in range(5)) for i in range(len(arr))]
    return max(value)  

def print_outcome(value):
    print(f"The binary value converted to decimal is:{value}")
    print(f"The highest fitness value is:{value**2}")

def initialize_population(population_size):
    return [[rand.randint(0, 1) for _ in range(5)] for _ in range(population_size)]

def fitness_measure(population):
    population_size = len(population)
    value = [sum(2**(4-j) * population[i][j] for j in range(5)) for i in range(len(population))]
    f_x = [v**2 for v in value]
    average = sum(f_x) / population_size
    expected_count = list(map(lambda x: x/average, f_x))
    actual_count = list(map(round, expected_count))
    parent = [population[i] for i in range(population_size) for j in range(actual_count[i])]
    rand.shuffle(parent)
    return parent

def crossover(parent, crossover_type):
    children = []
    n_parents = len(parent)
    if (crossover_type == 1):
        point1, point2 = 2, 4
        for i in range(0, n_parents, 2):
            parent1, parent2 = parent[i], parent[i+1]
            child1 = parent1[:point1] + parent2[point1:point2] + parent1[point2:]
            child2 = parent2[:point1] + parent1[point1:point2] + parent2[point2:]
            children.extend([child1, child2])
    elif (crossover_type == 0):
        point = 2
        for i in range(n_parents // 2):
            parent1, parent2 = parent[2*i], parent[2*i+1]
            child1 = parent1[:point] + parent2[point:]
            child2 = parent2[:point] + parent1[point:]
            children.extend([child1, child2])
    return children

def mutation(children, mutation_type):
    # mutated_children = []
    mutation_rate = 0.5
    if (mutation_type == 1):
        for child in children:
            if rand.random() < mutation_rate:
                swap1, swap2 = rand.sample(range(5), 2)
                child[swap1], child[swap2] = child[swap2], child[swap1]
    elif (mutation_type == 0):
        for child in children:
            if rand.random() < mutation_rate:
                child[-1] = 1 if child[-1] == 0 else 0




# -------------------------------------------------------
# main execution of code starts here
# -------------------------------------------------------




p = int(input("Enter the value of p:"))
c = int(input("Enter the value of c(0 or 1):"))
m = int(input("Enter the value of m(0 or 1):"))
t = int(input("Enter the value of t(0 or 1):"))
if (t == 0):
    x = int(input("Enter the value of x:"))
else:
    i = int(input("Enter the value of i:"))

# p = 10
# c = 0
# m = 0
# x = 10
# t = 0
# i = 300

maximum  = 0
population = initialize_population(p)

if (t == 1):
    for _ in range (i):
        parent = fitness_measure(population)
        children = crossover(parent, c)
        mutation(children,m)
        population = children
        iteration_outcome = max_in_list(children)
        if (maximum < iteration_outcome):
            maximum = iteration_outcome  
elif (t == 0):
    count = 0
    j = 0
    while True:
        j = j + 1
        parent = fitness_measure(population)
        children = crossover(parent, c)
        mutation(children,m)
        # print(children)
        population = children
        iteration_outcome = max_in_list(children)
        # print(iteration_outcome)
        if (maximum < iteration_outcome):
            count = 0
            maximum = iteration_outcome
        else:
            count = count + 1
            if (count == x):
                print(f"Algorithm is stopping after {j} iterations....")
                break
print_outcome(maximum)
