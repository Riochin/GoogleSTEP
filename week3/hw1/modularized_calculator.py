#! /usr/bin/python3

def read_number(line, index):
    number = 0
    while index < len(line) and line[index].isdigit():
        number = number * 10 + int(line[index])
        index += 1
    if index < len(line) and line[index] == '.':
        index += 1
        decimal = 0.1
        while index < len(line) and line[index].isdigit():
            number += int(line[index]) * decimal
            decimal /= 10
            index += 1
    token = {'type': 'NUMBER', 'number': number}
    return token, index


def read_plus(line, index):
    token = {'type': 'PLUS'}
    return token, index + 1


def read_minus(line, index):
    token = {'type': 'MINUS'}
    return token, index + 1

def read_times(line, index):
    token = {'type': 'TIMES' }
    return token, index + 1

def read_divides(line, index):
    token = {'type': 'DIVIDES'}
    return token, index + 1

def read_para_open(line, index):
    token = {'type': 'PARA_OPEN'}
    return token, index + 1

def read_para_close(line, index):
    token = {'type': 'PARA_CLOSE'}
    return token, index + 1

def tokenize(line):
    tokens = []
    index = 0
    while index < len(line):
        if line[index].isdigit():
            (token, index) = read_number(line, index)
        elif line[index] == '+':
            (token, index) = read_plus(line, index)
        elif line[index] == '-':
            (token, index) = read_minus(line, index)
        elif line[index] == '/':
            (token, index) = read_divides(line, index)
        elif line[index] == '*':
            (token, index) = read_times(line, index)
        elif line[index] == ' ': # 💡 スペースはスキップしちゃう
            index += 1
        elif line[index] == '(':
            (token, index) = read_para_open(line, index)
        elif line[index] == ')':
            (token, index) = read_para_close(line, index)
        else:
            print('Invalid character found: ' + line[index])
            exit(1)
        tokens.append(token)
    return tokens


def evaluate(tokens):
    # 💡 --- 第零段階: カッコの処理（再帰呼び出し） ---
    tokens_processed_parentheses = []
    i = 0
    while i < len(tokens):
        if tokens[i]['type'] == 'PARA_OPEN':
            open_count = 1
            j = i+1
            sub_expression_tokens = []

            while j < len(tokens) and open_count > 0:
                if tokens[j]['type'] == 'PARA_OPEN':
                    open_count += 1
                elif tokens[j]['type'] == 'PARA_CLOSE':
                    open_count -= 1

                if open_count > 0:
                    sub_expression_tokens.append(tokens[j])
                j += 1
            if open_count != 0:
                print("Error: Mismatched parentheses!")
                exit(1)

            sub_answer = evaluate(sub_expression_tokens)
            tokens_processed_parentheses.append(
                {'type':'NUMBER', 'number': sub_answer}
            )
            i = j
        else:
            tokens_processed_parentheses.append(tokens[i])
            i += 1
            
    i = 0
    processed_tokens = []
    tokens = tokens_processed_parentheses

    # 💡 --- 第一段階: 積・商の処理 ---
    while i < len(tokens):
        # print(tokens[i])
        if tokens[i]['type'] == 'TIMES':
            last_number_token = processed_tokens.pop()
            result = last_number_token['number'] * tokens[i+1]['number']
            processed_tokens.append({'type': 'NUMBER', 'number':result})
            i += 2
        elif tokens[i]['type'] =='DIVIDES':
            last_number_token = processed_tokens.pop()
            if tokens[i+1]['number'] == 0: # 💡ゼロ除算を防ぐ
                print("Error: Division by zero!!")
                exit(1)
            result = last_number_token['number'] / tokens[i+1]['number']
            processed_tokens.append({'type': 'NUMBER', 'number': result})
            i += 2
        else: # 乗算・除算以外のトークンはそのまま追加
            processed_tokens.append(tokens[i])
            i += 1

    # 💡 --- 第二段階: 和・差の処理 ---
    processed_tokens.insert(0, {'type':'PLUS'})
    answer = 0
    index = 1

    while index < len(processed_tokens):
        # print(tokens[index])
        if processed_tokens[index]['type'] == 'NUMBER':
            if processed_tokens[index - 1]['type'] == 'PLUS':
                answer += processed_tokens[index]['number']
            elif processed_tokens[index - 1]['type'] == 'MINUS':
                answer -= processed_tokens[index]['number']
            else:
                print('Invalid syntax')
                exit(1)
        index += 1
    return answer


def test(line):
    tokens = tokenize(line)
    actual_answer = evaluate(tokens)
    expected_answer = eval(line)
    if abs(actual_answer - expected_answer) < 1e-8:
        print("PASS! (%s = %f)" % (line, expected_answer))
    else:
        print("FAIL! (%s should be %f but was %f)" % (line, expected_answer, actual_answer))


# Add more tests to this function :)
# hw2
def run_test():
    print("==== Test started! ====")
    test("1+2")
    test("1.0+2.1-3")
    test("2*3")          # 新しいテスト
    test("6/2")          # 新しいテスト
    test("1+2*3")       # 優先順位のテスト
    test("10-4/2")      # 優先順位のテスト
    test("5*2+3")       # 優先順位のテスト
    test("10/2-1")      # 優先順位のテスト
    test("1+2*3-4/2") # 複合テスト
    test("7+5*2-12/3+1")
    # test("0/0") # ゼロ除算テスト（エラーになることを期待）

    # --- 追加のテストケース (カッコ) ---
    test("(1+2)*3")       # カッコが積より優先
    test("10/(4-2)")      # カッコが商より優先
    test("(5+2)*(3-1)")   # 複数のカッコ
    test("((1+2)*3)+4")   # ネストされたカッコ
    test("1+(2*(3+4))")   # ネストされたカッコと外側の演算
    test("1*(2+(3*4))")   # ネストされたカッコと外側の演算
    # test("(1+2")         # 括弧の不一致 (エラーになることを期待)

    print("==== Test finished! ====\n")

run_test()

while True:
    print('> ', end="")
    line = input()
    tokens = tokenize(line)
    answer = evaluate(tokens)
    print("answer = %f\n" % answer)
