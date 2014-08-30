package main

import (
	"./passenger"
	"./smrt"
	"./webService"
	"github.com/go-martini/martini"
)

func main() {
	martiniClassic := martini.Classic()
	sPassenger := passenger.NewPassenger()
	//sSMRT := smrt.NewSMRT()
	webService.RegisterWebService(sPassenger, martiniClassic)
	//webService.RegisterWebService(sPassenger, martiniClassic)
	martiniClassic.Run()
}
