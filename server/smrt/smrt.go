package smrt

import (
	"../passenger"
)

type SMRTReq struct {
	BusName          string
	StartBusStopName string
	DestBusStopName  string
}
type SMRTResp struct {
	NumPassengers []int
}
type SMRT struct {
	PassengerObj *passenger.Passenger
}

func NewSMRT(_passenger *passenger.Passenger) *SMRT {
	return &SMRT{
		_passenger,
	}
}

func (g *SMRT) ProcessReq(req SMRTReq) SMRTResp {
	var resp SMRTResp
	return resp
}
