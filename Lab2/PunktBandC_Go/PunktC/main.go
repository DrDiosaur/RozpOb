package main

import (
	"fmt"
	"math"
)

var chiEnergy []int

func init(){
	chiEnergy = []int{2,6,523,12,6,52,987,43, 533, 2342, 4423, 423, 432, 64, 42, 109}
}

func chooseWinner(array []int) int{
	buffer := make(chan int)
	for j:=0;j<int(math.Log2(float64(len(array))));j++{
		for i:=0;i<len(array);i+=2*int(math.Pow(2, float64(j))){
			go func() {
				if array[i] < array[i+int(math.Pow(2, float64(j)))]{
					buffer <- array[i+int(math.Pow(2, float64(j)))]
				}else {
					buffer <- array[i]
				}
			}()
			array[i] = <- buffer
			//fmt.Println(j, "-> current -> i =", array[i])
		}
	}
	return array[0]
}

func main() {

	winner := chooseWinner(chiEnergy)
	fmt.Println("The winner is monk with Chi Energy =", winner)
}
