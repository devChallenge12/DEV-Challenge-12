package main

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

const REPLICATIONS_ENV = "REPLICATIONS"

func main() {
	replications, err := GetReplicationsFromEnv()
	if err != nil {
		log.Fatal(err)
	}

	service := &Service{
		storage:      NewStorage(),
		replications: replications,
	}
	controller := &Controller{service}

	log.Fatal(RunNewServer(controller, ":80"))
}

func RunNewServer(controller *Controller, port string) error {
	r := mux.NewRouter()
	r.HandleFunc("/", controller.Dump).Methods("GET")
	r.HandleFunc("/{name}", controller.Get).Methods("GET")
	r.HandleFunc("/{name}", controller.Update).Methods("PUT")
	r.HandleFunc("/{name}", controller.Remove).Methods("DELETE")

	r.HandleFunc("/internal/{name}", controller.Get).Methods("GET")
	r.HandleFunc("/internal/{name}", controller.Update).Methods("PUT")
	r.HandleFunc("/internal/{name}", controller.Remove).Methods("DELETE")

	return http.ListenAndServe(port, r)
}
