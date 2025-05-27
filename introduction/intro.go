package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	input := scanner.Text()
	
	// スペースで区切られた文字列を数字の配列に変換
	strNums := strings.Fields(input) // スペースで区切られた文字列を配列に変換"1 2 3 4 5" => ["1","2","3","4","5"]
	stream := make([]int, len(strNums)) // 数字の配列を作成[0,0,0,0,0]
	for i, str := range strNums { // 配列の各要素を数字に変換[1,2,3,4,5]
		num, _ := strconv.Atoi(str) 
		stream[i] = num // 数字を配列に格納[0,0,0,0,0] => [1,..,n,0,0]
	}

	majority := findMajority(stream)
	fmt.Println(majority)
}

func findMajority(stream []int) int {
	count := 0
	candidate := 0

	for _, num := range stream {
		if count == 0 {
			candidate = num
			count = 1
		} else if candidate == num {
			count++
		} else {
			count--
		}
	}
	return candidate
}
