def quick_sort(array, left, right):
    pivot = array[int((left + right) / 2)]
    i = left
    j = right
    print(right, left, pivot)
    while True:
        # print(right, left)
        while pivot > array[i]:
            i += 1
        print(right, left)
        while pivot < array[j]:
            j -= 1
        if i >= j:
            break
        temp = array[i]
        array[i] = array[j]
        array[j] = temp
        i += 1
        j -= 1
    if left < i - 1:
        quick_sort(array, left, i - 1)
    if right > j + 1:
        quick_sort(array, j + 1, right)

def sort(array):
    quick_sort(array, 0, len(array) - 1)

def binary_search(array, target):
    left = 0
    right = len(array) - 1
    
    while left <= right:
        middle = int((left + right) / 2)
        print(left, right, middle)
        if array[middle] == target:
            return True
        if target < array[middle]:
            right = middle - 1
        else:
            left = middle + 1
    return False

def check_binary_search():
    while True:
        print("Number:", end="")
        target = int(input())

        # Search if the target number is contained or not
        found = binary_search(array, target)
        if found:
            print("Found")
        else:
            print("Not found")

# Read input data
print("Array: ", end="")
array = list(map(int, input().split()))

# Sort the array
sort(array)
 
print(array) # デバッグのために追加

check_binary_search()

