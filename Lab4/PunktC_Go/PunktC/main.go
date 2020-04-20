package main

import (
	"fmt"
	"math/rand"
	"time"
)

type City struct {
	name string
}

type Route struct {
	destination string
	price float64
}

var cities []City
var routes []Route

func changePrice(readWriteLock chan int){
	i := 0
	for  {
		time.Sleep(time.Second)
		if i!=0{
			<-readWriteLock
		}
		random := rand.Intn(len(routes))
		fmt.Println("Price of route", routes[random].destination, "->", routes[random].price, "before changing")
		routes[random].price = float64(rand.Intn(100))
		fmt.Println("Price of route", routes[random].destination, "->", routes[random].price, "after changing")
		fmt.Println("")
		i++
		time.Sleep(time.Second)
		readWriteLock <- 1
	}
}

func changeRoutes(readWriteLock chan int){
	for {
		<-readWriteLock
		random := rand.Intn(2)

		if random==0{
			randIndex := rand.Intn(len(routes))
			fmt.Println("Deleting route with destination -> ", routes[randIndex].destination)
			routes = append(routes[:randIndex], routes[randIndex+1:]...)
			//fmt.Println(routes)
		}else {
			fmt.Println("Adding new route")
			randA := rand.Intn(len(cities))
			cityA := cities[randA].name

			randB := rand.Intn(len(cities))
			if randB == randA {
				for randB == randA{
					randB = rand.Intn(len(cities))
				}
			}
			cityB := cities[randB].name

			dest := "from: " + cityA + ", to: " + cityB
			price := float64(rand.Intn(100))
			fmt.Println("Added route", dest, "with price", price)
			routes = append(routes, Route{dest, price})
		}
		time.Sleep(time.Second)
		fmt.Println("")
		readWriteLock <- 1
	}
}

func changeCities(readWriteLock chan int){
	for {
		random := rand.Intn(2)

		<-readWriteLock
		if random==0{
			randIndex := rand.Intn(len(cities))
			fmt.Println("Deleting city name -> ", cities[randIndex].name)
			cities = append(cities[:randIndex], cities[randIndex+1:]...)
			//fmt.Println(cities)
		}else {
			fmt.Println("Adding new city")
			//randLen := rand.Intn(len(letterRunes))
			newCityName := randomName(2)
			cities = append(cities, City{newCityName})
			fmt.Println("Added city", newCityName)
		}
		time.Sleep(time.Second)
		fmt.Println("")
		readWriteLock <- 1
	}
}

var letterRunes = []rune("ABCDEFGHIJKLMNOPQRSTUVWXYZ")

func randomName(n int) string {
	b := make([]rune, n)
	for i := range b {
		b[i] = letterRunes[rand.Intn(len(letterRunes))]
	}
	return string(b)
}

func findWay(readWriteLock chan int){
	cityA := "A"
	cityB := "B"

	for  {
		<-readWriteLock

		count := 0
		found := false

		for i:=0;i<len(cities);i++{
			if cities[i].name == cityA {
				count++
			} else if cities[i].name == cityB{
				count++
			}

			if count == 2{
				break
			}
		}

		if count == 2{
			for i:=0;i<len(routes);i++{
				substrA := routes[i].destination[6:8]
				substrB := routes[i].destination[13:15]
				if substrA == (cityA+","){
					if substrB == (cityB+"."){
						fmt.Println("Price of route", routes[i].destination, "equals", routes[i].price)
						found = true
						break
					}
				}
			}

			if !found{
				fmt.Println("Route between city", cityA, "and city", cityB, "does not exist")
			}
		}else {
			fmt.Println("City", cityA, "or city", cityB, "does not exist")
		}

		time.Sleep(time.Second)
		fmt.Println("")
		readWriteLock <- 1
	}
}

func init()  {
	rand.Seed(time.Now().UnixNano())
	cities = make([]City, 0)
	routes = make([]Route, 0)
	cities = append(cities, City{"A"})
	cities = append(cities, City{"B"})
	cities = append(cities, City{"C"})
	cities = append(cities, City{"D"})
	cities = append(cities, City{"E"})
	routes = append(routes, Route{"from: A, to: B.", 13.5})
	routes = append(routes, Route{"from: B, to: C.", 23.1})
	routes = append(routes, Route{"from: C, to: D.", 56.9})
	routes = append(routes, Route{"from: D, to: E.", 9.6})
}

func main() {
	rwLock := make(chan int)
	go changePrice(rwLock)
	go changeCities(rwLock)
	go changeRoutes(rwLock)
	findWay(rwLock)
}
