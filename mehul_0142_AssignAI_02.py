def is_valid_assignment(letters, inputStrings, sumString):
    word_values = [sum([letters[char] * (10 ** (len(word) - idx - 1)) for idx, char in enumerate(word)]) for word in inputStrings]
    sum_value = sum([letters[char] * (10 ** (len(sumString) - idx - 1)) for idx, char in enumerate(sumString)])
    return sum(word_values) == sum_value


def solve(inputStrings, sumString):
    unique_chars = set(''.join(inputStrings + [sumString]))
    letters = {char: -1 for char in unique_chars}
    
    def assign_digits(char_idx):
        nonlocal letters
        if char_idx == len(unique_chars):
            return is_valid_assignment(letters, inputStrings, sumString)
        char = list(unique_chars)[char_idx]
        for digit in range(10):
            if digit not in letters.values():
                letters[char] = digit
                if assign_digits(char_idx + 1):
                    return True
                letters[char] = -1
        return False
    
    if assign_digits(0):
        print("\nYES, the solution exists for the given cryptarithmetic problem")
        print("The solution is:\n")
        print("The value of letters are => ", end = "")
        print(f"{letters}\n")
        for word in inputStrings:
            print(f"{word}: {' '.join(str(letters[char]) for char in word)}")
        print(f"{sumString}: {' '.join(str(letters[char]) for char in sumString)}")
    else:
        print("\nNO, the solution for this problem does not exists.")


inputStrings = [input(f"Enter the input string {i+1}: ") for i in range(int(input("Enter number of input strings: ")))]
sumString = input("Enter the sum word: ")
solve(inputStrings, sumString)