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

func NewPassenger() *Passenger {
	return &Passenger{
		make([]*PassengerEntry, 0),
		new(sync.Mutex),
	}
}

func (g *Passenger) AddEntry(busID []int, originBusStopID int, destinationBusStopID int) int {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	newId := len(g.passengerPool)
	newEntry := &PassengerEntry{
		newId,
		busID,
		originBusStopID,
		destinationBusStopID,
	}

	g.passengerPool = append(g.passengerPool, newEntry)
	return newId
}

func (g *Passenger) RemoveEntry(id int) error {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	if id < 0 || id >= len(g.passengerPool) ||
		g.passengerPool[id] == nil {
		return fmt.Errorf("invalid id")
	}

	g.passengerPool[id] = nil
	g.passengerPool = append(g.passengerPool[:id], g.passengerPool[id+1:]...)

	return nil
}

func (g *Passenger) GetEntry(id int) (*PassengerEntry, error) {
	if id < 0 || id >= len(g.passengerPool) ||
		g.passengerPool[id] == nil {
		return nil, fmt.Errorf("invalid id")
	}

	return g.passengerPool[id], nil
}

func (g *Passenger) GetAllEntries() []*PassengerEntry {
	entries := make([]*PassengerEntry, 0)

	// Iterate through all existig entries.
	for _, entry := range g.passengerPool {
		if entry != nil {
			// Entry is not nil, so we want to return it.
			entries = append(entries, entry)
		}
	}

	return entries
}

// RemoveAllEntries removes all entries from the Guest Book.
func (g *Passenger) RemoveAllEntries() {
	// Acquire our lock and make sure it will be released.
	g.mutex.Lock()
	defer g.mutex.Unlock()

	// Reset guestbook to a new empty one.
	g.passengerPool = []*PassengerEntry{}
}
