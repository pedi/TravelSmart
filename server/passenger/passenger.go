package passenger

import (
	"fmt"
	"sync"
)

type PassengerEntry struct {
	ID                   int
	busID                []int
	originBusStopID      int
	destinationBusStopID int
}

type Passenger struct {
	passengerPool []*PassengerEntry
	mutex         *sync.Mutex
}

func NewPassengers() *Passenger {
	return &Passenger{
		make([]*PassengerEntry, 0),
		new(sync.Mutex),
	}
}
