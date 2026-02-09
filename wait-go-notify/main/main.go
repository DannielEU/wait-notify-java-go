package main

import (
	"fmt"
	"runtime"
	"time"
)

func Primeworker(jobs <-chan int, results chan<- bool) {
	for n := range jobs {
		results <- isPrime(n)
	}

}

func isPrime(n int) bool {
	if n < 2 {
		return false
	}
	for i := 2; i*i <= n; i++ {
		if n%i == 0 {
			return false
		}
	}
	return true
}

func main() {
	fmt.Println("// Concurrente...")
	start := time.Now()
	prime := 0
	jobs := make(chan int, 10000)
	results := make(chan bool, 10000)

	worker := runtime.NumCPU()

	for n := 0; n < worker; n++ {
		go Primeworker(jobs, results)
	}

	go func() {
		for i := 0; i < 1_000_000_000; i++ {
			jobs <- i
		}
		close(jobs)
	}()

	for i := 0; i < 1_000_000_000; i++ {
		if <-results {
			prime++
		}
	}

	fmt.Println("numero de primos: ", prime)
	fmt.Println("tiempo: ", time.Since(start))
	fmt.Println("// Secuencial...")

	prime = 0

	start = time.Now()

	for i := 0; i < 1_000_000_000; i++ {
		if isPrime(i) {
			prime++
		}
	}

	fmt.Println("numero de primos: ", prime)
	fmt.Println("tiempo: ", time.Since(start))

}
