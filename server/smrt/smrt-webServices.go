package smrt

import (
	"encoding/json"
	"github.com/go-martini/martini"
	"io/ioutil"
	"net/http"
)

func (g *SMRT) GetPath() string {
	return "/smrt"
}

func (g *SMRT) WebDelete(params martini.Params) (int, string) {

	return http.StatusBadRequest, "invalid command"

}

func (g *SMRT) WebGet(params martini.Params) (int, string) {
	return http.StatusBadRequest, "invalid command"
}

func (g *SMRT) WebPost(params martini.Params,
	req *http.Request) (int, string) {
	defer req.Body.Close()
	requestBody, err := ioutil.ReadAll(req.Body)
	if err != nil {
		return http.StatusInternalServerError, "internal error"
	}

	if len(params) != 0 {
		return http.StatusMethodNotAllowed, "method not allowed"
	}
	var smrtReq SMRTReq
	err = json.Unmarshal(requestBody, &smrtReq)
	if err != nil {
		return http.StatusBadRequest, "invalid JSON data"
	}
	var resp = g.ProcessReq(smrtReq)
	encodedResp, err := json.Marshal(resp)
	return http.StatusOK, string(encodedResp)
}
