package passenger

import (
	"fmt"
	"sync"
	"time"
)

type PassengerEntry struct {
	EZLinkID             int   `json:"EZLinkID"`
	BusID                []int `json:"BusID"`
	OriginBusStopID      int   `json:"OriginBusStopID"`
	DestinationBusStopID int   `json:"DestinationBusStopID"`
	CreatedTime          time.Time
}

type Passenger struct {
	PassengerPool []*PassengerEntry
	mutex         *sync.Mutex
	EZLink2ID     map[int]int
}

func NewPassenger() *Passenger {
	return &Passenger{
		make([]*PassengerEntry, 0),
		new(sync.Mutex),
		make(map[int]int),
	}
}

func (g *Passenger) AddEntry(EZLinkID int, busID []int, originBusStopID int, destinationBusStopID int) {
	g.mutex.Lock()
	defer g.mutex.Unlock()
	var _, find = g.EZLink2ID[EZLinkID]
	fmt.Println(find)
	if find {
		var id = g.EZLink2ID[EZLinkID]
		if g.PassengerPool[id] != nil {
			g.PassengerPool[id].BusID = busID
			g.PassengerPool[id].OriginBusStopID = originBusStopID
			g.PassengerPool[id].DestinationBusStopID = destinationBusStopID
			g.PassengerPool[id].CreatedTime = time.Now()
		} else {
			g.PassengerPool[id] = &PassengerEntry{
				EZLinkID,
				busID,
				originBusStopID,
				destinationBusStopID,
				time.Now(),
			}
		}

	} else {
		newEntry := &PassengerEntry{
			EZLinkID,
			busID,
			originBusStopID,
			destinationBusStopID,
			time.Now(),
		}

		g.EZLink2ID[EZLinkID] = len(g.PassengerPool)
		g.PassengerPool = append(g.PassengerPool, newEntry)
	}
}

func (g *Passenger) RemoveEntryByID(id int) error {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	if id < 0 || g.PassengerPool[id] == nil {
		return fmt.Errorf("invalid id")
	}
	g.PassengerPool[id] = nil

	return nil
}

func (g *Passenger) RemoveEntryByEZLinkID(ezlinkid int) error {
	g.mutex.Lock()
	defer g.mutex.Unlock()
	var id, find = g.EZLink2ID[ezlinkid]
	if !find || id < 0 || g.PassengerPool[id] == nil {
		return fmt.Errorf("invalid id")
	}
	g.PassengerPool[id] = nil

	return nil
}

func (g *Passenger) GetEntryByID(id int) (*PassengerEntry, error) {
	if id < 0 || g.PassengerPool[id] == nil {
		return nil, fmt.Errorf("invalid id")
	}

	return g.PassengerPool[id], nil
}

func (g *Passenger) GetEntryByEZLinkID(ezlinkid int) (*PassengerEntry, error) {
	var id = g.EZLink2ID[ezlinkid]
	if id < 0 || g.PassengerPool[id] == nil {
		return nil, fmt.Errorf("invalid id")
	}

	return g.PassengerPool[id], nil
}

func (g *Passenger) GetAllEntries() []*PassengerEntry {
	entries := make([]*PassengerEntry, 0)

	for _, entry := range g.PassengerPool {
		if entry != nil {
			entries = append(entries, entry)
		}
	}

	return entries
}

func (g *Passenger) RemoveAllEntries() {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	g.PassengerPool = []*PassengerEntry{}
}
