package main

import (
	"./passenger"
	"./smrt"
	"./webService"
	"fmt"
	"github.com/go-martini/martini"
	"time"
)

func deleteExpiredPassenger(g *passenger.Passenger) {
	for {
		time.Sleep(60 * time.Second)
		fmt.Printf("%v deleteExpiredPassenger\n", time.Now())
		for index, p := range g.PassengerPool {
			if p == nil {
				continue
			}
			fmt.Printf("%v %v\n", index, time.Since(p.CreatedTime))
			if time.Since(p.CreatedTime) >= 60*time.Minute {
				g.RemoveEntryByID(index)
			}
		}
	}
}
func main() {
	martiniClassic := martini.Classic()
	sPassenger := passenger.NewPassenger()
	sSMRT := smrt.NewSMRT(sPassenger)
	go deleteExpiredPassenger(sPassenger)
	webService.RegisterWebService(sPassenger, martiniClassic)
	webService.RegisterWebService(sSMRT, martiniClassic)
	martiniClassic.Run()
}
