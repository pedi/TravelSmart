package smrt

import (
	"../passenger"
	"fmt"
)

type SMRTReq struct {
	BusName                string `json:"BusName"`
	OriginBusStopName      string `json:"OriginBusStopName"`
	DestinationBusStopName string `json:"DestinationBusStopName"`
}
type SMRTResp struct {
	NumPassengers []BusStation
}
type SMRT struct {
	PassengerObj *passenger.Passenger
	route        Route
}
type BusStation struct {
	Name          string
	BusName       []string
	BusName2Count map[string]int
}

type Route struct {
	stations []BusStation
	order    map[string]int
}

func NewSMRT(_passenger *passenger.Passenger) *SMRT {
	var route Route
	route.stations = append(route.stations,
		BusStation{"opp Buona Vista Station", []string{"74", "91", "92", "95", "191", "196", "198", "200"}, make(map[string]int)})
	route.stations = append(route.stations,
		BusStation{"opp Anglo-Chinese Junior College", []string{"74", "91", "92", "95", "191", "196", "198", "200"}, make(map[string]int)})
	route.stations = append(route.stations,
		BusStation{"Ayer Rajah Ind Est", []string{"14", "33", "92", "95", "166", "198", "200"}, make(map[string]int)})
	route.stations = append(route.stations,
		BusStation{"Kent Ridge Station", []string{"95"}, make(map[string]int)})
	route.order = make(map[string]int)
	route.order["opp Buona Vista Station"] = 0
	route.order["opp Anglo-Chinese Junior College"] = 1
	route.order["Ayer Rajah Ind Est"] = 2
	route.order["Kent Ridge Station"] = 3

	fmt.Printf("Routes: %v\n", route)
	return &SMRT{
		_passenger,
		route,
	}
}

func (g *SMRT) ProcessReq(req SMRTReq) SMRTResp {
	var resp SMRTResp
	var find bool
	var findIdx int
	find = false
	findIdx = 0
	for index, station := range g.route.stations {
		fmt.Printf("%v - %v - %v", station.Name, req.OriginBusStopName, station.Name == req.OriginBusStopName)
		if station.Name == req.OriginBusStopName {
			find = true
			findIdx = index
			break
		}
	}
	fmt.Printf("%v %v\n", find, findIdx)
	if find {
		for i := findIdx; i < len(g.route.stations); i++ {
			g.route.stations[i].BusName2Count = make(map[string]int)
		}
		for i := findIdx; i < len(g.route.stations); i++ {
			for j := 0; j < len(g.PassengerObj.PassengerPool); j++ {
				if g.route.order[g.PassengerObj.PassengerPool[j].OriginBusStopName] >= g.route.order[req.OriginBusStopName] &&
					g.route.order[g.PassengerObj.PassengerPool[j].DestinationBusStopName] <= g.route.order[req.DestinationBusStopName] {
					g.route.stations[i].BusName2Count[g.PassengerObj.PassengerPool[j].BusName]++
				}
			}
			resp.NumPassengers = append(resp.NumPassengers, g.route.stations[i])
			if g.route.stations[i].Name == req.DestinationBusStopName {
				break
			}
		}
	}
	return resp
}
