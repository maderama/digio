package main

import (
	"fmt"
	"log"
	"os"
	"regexp"
	"sort"
)

func main() {
	body, err := os.ReadFile("programming-task-example-data.log")
	if err != nil {
		log.Fatalf("unable to read file: %v", err)
	}

	str1 := string(body)

	re := regexp.MustCompile(`(?m)^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}`)

	mapValue := map[string]int{}

	submatchall := re.FindAllString(str1, -1)
	for _, element := range submatchall {
		if _, ok := mapValue[element]; ok {
			mapValue[element] += 1
		} else {
			mapValue[element] = 1
		}
		// mapValue[element] = 1
		// i++
		// fmt.Println(element)
	}
	// Create slice of key-value pairs
	pairs := make([][2]interface{}, 0, len(mapValue))
	for k, v := range mapValue {
		pairs = append(pairs, [2]interface{}{k, v})
	}

	// Sort slice based on values
	sort.Slice(pairs, func(i, j int) bool {
		return pairs[i][1].(int) > pairs[j][1].(int)
	})

	// Extract sorted keys
	keys := make([]string, len(pairs))
	for i, p := range pairs {
		keys[i] = p[0].(string)
	}

	i := 0
	// Print sorted map
	println("Top 3 IP addresses: ")
	for _, k := range keys {
		if i < 3 {
			fmt.Printf("%s\n", k)
			// fmt.Printf("%s: %d\n", k, mapValue[k])
			i += 1
		} else {
			break
		}
	}
	fmt.Println("Number of unique IP addresses:", len(mapValue))
	// fmt.Printf("Top 3 IP addresses: %v\n", mapValue)
}

// https://stackoverflow.com/questions/52514821/how-to-extract-x-top-int-values-from-a-map-in-golang
