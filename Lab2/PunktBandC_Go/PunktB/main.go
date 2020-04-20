package main

import "fmt"

type Property struct {
	price float64
	weight float64
}

var StorageSize = 20
var totalCost = 0.0
var storage []Property
var car []Property

func takeFromStorage(storage []Property, buffer chan Property, flag chan []Property){
	for i:=0; i < StorageSize; i++{
		currProp := storage[0]
		storage = append(storage[1:])
		buffer <- currProp
	}
	flag <- storage
}

func putInCar(car []Property, buffer chan Property, flag chan []Property){
	sumBuffer := make(chan Property)
	for i:=0;i< StorageSize;i++{
		go calculateTotalPrice(buffer, sumBuffer)
		car = append(car, <-sumBuffer)
	}
	flag <- car
}

func calculateTotalPrice(buffer chan Property, sumBuffer chan Property){
	currProp := <-buffer
	totalCost += currProp.price
	sumBuffer <- currProp
}

func init(){
	storage = make([]Property, 0)
	car = make([]Property, 0)
	for i:= 0; i < StorageSize; i++{
		prop := Property{10.0 + float64(i), float64(i)/10.0}
		storage = append(storage, prop)
	}
}

func main() {
	fmt.Println("Properties in warehouse before theft:", len(storage))
	fmt.Println("Properties in car before theft:", len(car))

	buffer := make(chan Property, 3)
	flag := make(chan []Property)
	flag2 := make(chan []Property)

	go takeFromStorage(storage, buffer, flag2)
	go putInCar(car, buffer, flag)

	storage = <-flag2
	car = <-flag

	fmt.Println("Properties in warehouse after theft:", len(storage))
	fmt.Println("Properties in car after theft:", len(car))
	fmt.Println("Total cost of all properties: $", totalCost)
}
