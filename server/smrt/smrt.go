package smrt

import (
	"../passenger"
	"fmt"
)

type SMRTReq struct {
	BusName                string
	OriginBusStopName      string
	DestinationBusStopName string
}
type SMRTResp struct {
	NumPassengers []int
}
type SMRT struct {
	PassengerObj *passenger.Passenger
	route        Route
}
type BusStation struct {
	Name    string
	BusName []string
}

type Route struct {
	stations []BusStation
}

func NewSMRT(_passenger *passenger.Passenger) *SMRT {
	var route Route
	route.stations = append(route.stations,
		BusStation{"opp Buona Vista Station", []string{"74", "91", "92", "95", "191", "196", "198", "200"}})
	route.stations = append(route.stations,
		BusStation{"opp Anglo-Chinese Junior College", []string{"74", "91", "92", "95", "191", "196", "198", "200"}})
	route.stations = append(route.stations,
		BusStation{"Ayer Rajah Ind Est", []string{"14", "33", "92", "95", "166", "198", "200"}})
	route.stations = append(route.stations,
		BusStation{"Kent Ridge Station", []string{"95"}})
	fmt.Printf("Routes: %v\n", route)
	return &SMRT{
		_passenger,
		route,
	}
}

func (g *SMRT) ProcessReq(req SMRTReq) SMRTResp {
	var resp SMRTResp
	return resp
}
