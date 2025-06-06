while True:
    print('> ', end="")
    line = input()
    answer = 0
    i = 0
    while i < len(line):
        if line[i].isdigit():
            number = 0
            while i < len(line) and line[i].isdigit():
                number = number * 10 + int(line[i])
                i += 1
            if is_plus:
                answer += number
            else:
                answer -= number
        elif line[i] == '+':
            is_plus = True
            i += 1
        elif line[i] == '-':
            i+=1
            is_plus = False  
        else:
            print('Invalid character found: ' + line[i])
            exit(1)
    print("answer = %d\n" % answer)