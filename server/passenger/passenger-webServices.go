package passenger

import (
	"encoding/json"
	"github.com/go-martini/martini"
	"io/ioutil"
	"net/http"
	"strconv"
)

func (g *Passenger) GetPath() string {
	return "/passenger"
}

func (g *Passenger) WebDelete(params martini.Params) (int, string) {
	if len(params) == 0 {
		g.RemoveAllEntries()

		return http.StatusOK, "collection deleted"
	}

	id, err := strconv.Atoi(params["id"])
	if err != nil {
		return http.StatusBadRequest, "invalid entry id"
	}

	err = g.RemoveEntryByEZLinkID(id)
	if err != nil {
		return http.StatusNotFound, "entry not found"
	}

	return http.StatusOK, "entry deleted"
}

func (g *Passenger) WebGet(params martini.Params) (int, string) {
	if len(params) == 0 {
		encodedEntries, err := json.Marshal(g.GetAllEntries())
		if err != nil {
			return http.StatusInternalServerError, "internal error"
		}

		return http.StatusOK, string(encodedEntries)
	}

	id, err := strconv.Atoi(params["id"])
	if err != nil {
		return http.StatusBadRequest, "invalid entry id"
	}

	entry, err := g.GetEntryByEZLinkID(id)
	if err != nil {
		return http.StatusNotFound, "entry not found"
	}

	encodedEntry, err := json.Marshal(entry)
	if err != nil {
		return http.StatusInternalServerError, "internal error"
	}
	return http.StatusOK, string(encodedEntry)
}

func (g *Passenger) WebPost(params martini.Params,
	req *http.Request) (int, string) {
	defer req.Body.Close()
	requestBody, err := ioutil.ReadAll(req.Body)
	if err != nil {
		return http.StatusInternalServerError, "internal error"
	}

	if len(params) != 0 {
		return http.StatusMethodNotAllowed, "method not allowed"
	}

	var passengerEntry PassengerEntry
	err = json.Unmarshal(requestBody, &passengerEntry)
	if err != nil {
		return http.StatusBadRequest, "invalid JSON data"
	}
	g.AddEntry(passengerEntry.EZLinkID, passengerEntry.BusName,
		passengerEntry.OriginBusStopName, passengerEntry.DestinationBusStopName)

	return http.StatusOK, "Submitted"
}
