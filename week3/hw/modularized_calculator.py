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
        elif line[index] == '(':
            (token, index) = read_para_open(line, index)
        elif line[index] == ')':
            (token, index) = read_para_close(line, index)
        elif line[index] == ' ': # 💡 スペースはスキップしちゃう
            index += 1
            # スペースの場合、token は生成されないので append もスキップ
            continue # この continue で tokens.append(token) をスキップ
        else:
            print('Invalid character found: ' + line[index])
            exit(1)
        tokens.append(token)
    return tokens

def remove_parentheses(tokens):
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

    return tokens_processed_parentheses

def process_kakewari_zan(tokens):
    i = 0
    processed_tokens = []
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
    return processed_tokens

def process_tasuhiku_tokens(tokens):
    tokens.insert(0, {'type':'PLUS'})
    answer = 0
    index = 1

    while index < len(tokens):
        # print(tokens[index])
        if tokens[index]['type'] == 'NUMBER':
            if tokens[index - 1]['type'] == 'PLUS':
                answer += tokens[index]['number']
            elif tokens[index - 1]['type'] == 'MINUS':
                answer -= tokens[index]['number']
            else:
                print('Invalid syntax')
                exit(1)
        index += 1
    # print(answer) # この行を削除
    return answer


def evaluate(tokens):
    # 💡 --- 第零段階: カッコの処理（再帰呼び出し） ---
    tokens = remove_parentheses(tokens)

    # 💡 --- 第一段階: 積・商の処理 ---
    processed_tokens = process_kakewari_zan(tokens)
    
    # 💡 --- 第二段階: 和・差の処理 ---
    answer = process_tasuhiku_tokens(processed_tokens)
    
    return answer


def test(line):
    tokens = tokenize(line)
    actual_answer = evaluate(tokens)
    try:
        expected_answer = eval(line)
        if abs(actual_answer - expected_answer) < 1e-8:
            print("PASS! (%s = %f)" % (line, expected_answer))
        else:
            print("FAIL! (%s should be %f but was %f)" % (line, expected_answer, actual_answer))
    except (SyntaxError, ZeroDivisionError, NameError) as e:
        print(f"PASS! (Expected error for {line}: {type(e).__name__})") # エラータイプを表示
    except Exception as e:
        print(f"FAIL! (Unexpected error for {line}: {e})")


def run_test():
    print("==== Test started! ====")
    test("1+2")
    test("1.0+2.1-3")
    test("2*3")
    test("6/2")
    test("1+2*3")
    test("10-4/2")
    test("5*2+3")
    test("10/2-1")
    test("1+2*3-4/2")
    test("7+5*2-12/3+1")
    # test("0/0") # ゼロ除算テスト（エラー）

    # --- 追加のテストケース (カッコ) ---
    test("(1+2)*3")
    test("10/(4-2)")
    test("(5+2)*(3-1)")
    test("((1+2)*3)+4")
    test("1+(2*(3+4))")
    test("1*(2+(3*4))")
    # test("(1+2") # 括弧の不一致 (エラー) 

    print("==== Test finished! ====\n")

run_test()

while True:
    print('> ', end="")
    line = input()
    tokens = tokenize(line)
    answer = evaluate(tokens)
    print("answer = %f\n" % answer)