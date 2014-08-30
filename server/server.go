package main

import (
	"github.com/codegangsta/martini"
	"passenger"
	"smrt"
	"webService"
)

func main() {
	martiniClassic := martini.Classic()
	sPassenger := passenger.NewPassenger()
	sSMRT := smrt.NewSMRT()
	webService.RegisterWebService(sPassenger, martiniClassic)
	webService.RegisterWebService(sPassenger, martiniClassic)
	martiniClassic.Run()
}
