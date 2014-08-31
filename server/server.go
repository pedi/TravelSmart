package main

import (
	"./passenger"
	"./smrt"
	"./webService"
	"fmt"
	"github.com/go-martini/martini"
	"github.com/martini-contrib/cors"
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
				//g.RemoveEntryByID(index)
			}
		}
	}
}
func main() {
	martiniClassic := martini.Classic()
	martiniClassic.Use(cors.Allow(&cors.Options{
		AllowOrigins:     []string{"*"},
		AllowMethods:     []string{"POST", "GET", "OPTIONS", "PUT", "DELETE"},
		AllowHeaders:     []string{"Origin"},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
	}))

	sPassenger := passenger.NewPassenger()
	sSMRT := smrt.NewSMRT(sPassenger)
	go deleteExpiredPassenger(sPassenger)
	webService.RegisterWebService(sPassenger, martiniClassic)
	webService.RegisterWebService(sSMRT, martiniClassic)
	martiniClassic.Run()
}
