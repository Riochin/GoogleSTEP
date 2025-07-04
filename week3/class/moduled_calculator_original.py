#! /usr/bin/python3

def read_number(line, index):
    number = 0
    while index < len(line) and line[index].isdigit():
        number = number * 10 + int(line[index])
        index += 1
    if line[index] == '.':
        (decimal, index) = read_decimal(line, index + 1)
        number += decimal
    token = {'type': 'NUMBER', 'number': number}
    return token, index

def read_decimal(line, index):
    number = 0
    decimal = 0.1
    while index < len(line) and line[index].isdigit():
        number += int(line[index]) * decimal
        decimal *= 0.1
        index += 1
    return number, index


def read_plus(line, index):
    token = {'type': 'PLUS'}
    return token, index + 1

def read_minus(line, index):
    token = {'type': 'MINUS'}
    return token, index + 1

def read_point(line, index):
    token = {'type': 'POINT'}
    return token, index + 1

def tokenize(line):
    """
    Tokenize the input line and return a list of tokens
    """
    tokens = []
    index = 0
    while index < len(line):
        if line[index].isdigit():
            (token, index) = read_number(line, index)
        elif line[index] == '+':
            (token, index) = read_plus(line, index)
        elif line[index] == '-':
            (token,index) = read_minus(line, index)
        elif line[index] == '.':
            (token, index) = read_point(line, index)
        else:
            print('Invalid character found: ' + line[index])
            exit(1)
        tokens.append(token)
    return tokens


def evaluate(tokens):
    """
    Evaluate the list of tokens and return a calculated result
    """
    answer = 0
    tokens.insert(0, {'type': 'PLUS'}) # Insert a dummy '+' token
    index = 1
    while index < len(tokens):
        if tokens[index]['type'] == 'NUMBER':
            if tokens[index - 1]['type'] == 'PLUS':
                answer += tokens[index]['number']
            elif tokens[index - 1]['type'] == 'MINUS':
                answer -= tokens[index]['number']
            else:
                print('Invalid syntax')
        index += 1
    return answer


while True:
    print('> ', end="")
    line = input()
    tokens = tokenize(line)
    answer = evaluate(tokens)
    print("answer = %d\n" % answer)