package main

import (
	"io/ioutil"
	"net/http"
	"os"
	"strings"

	"github.com/gorilla/mux"
)

type Controller struct {
	service ServiceInterface
}

func (c *Controller) Dump(w http.ResponseWriter, r *http.Request) {
	query := r.URL.Query()

	fileName := "default"
	if iFileName, ok := query["fileName"]; ok {
		fileName = iFileName[0]
	}

	filePath, err := c.service.Dump()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	input, err := os.Open(filePath)
	if err != nil {
		http.Error(w, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
		return
	}

	info, err := input.Stat()
	if err != nil {
		http.Error(w, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Disposition", "attachment; filename="+fileName)
	http.ServeContent(w, r, fileName, info.ModTime(), input)
}

func (c *Controller) Get(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)

	value, err := c.service.Get(vars["name"])
	if err != nil {
		http.Error(w, err.Error(), http.StatusNotFound)
		return
	}

	w.Write([]byte(value))
	w.WriteHeader(http.StatusOK)
}

func (c *Controller) Update(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)

	body, err := ioutil.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	err = c.service.Update(vars["name"], string(body), shouldReplicate(r.URL.Path))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
}

func (c *Controller) Remove(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)

	err := c.service.Remove(vars["name"], shouldReplicate(r.URL.Path))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
}

func shouldReplicate(path string) bool {
	return !strings.Contains(path, "/internal/");
}