package main

import (
	"fmt"
	"net/http"
	"testing"
	"time"
)

func TestReplication(t *testing.T) {
	replica1 := &Replication{
		client: &http.Client{},
	}
	replica1.SetDsn("127.0.0.1", "8001")

	replica2 := &Replication{
		client: &http.Client{},
	}
	replica2.SetDsn("127.0.0.1", "8002")

	replica3 := &Replication{
		client: &http.Client{},
	}
	replica3.SetDsn("127.0.0.1", "8003")

	// Up 3 replications.
	fmt.Println("Up 3 replications for testing.")
	go RunNewServer(&Controller{&Service{
		storage: NewStorage(),
		replications: []ReplicationInterface{
			replica2,
			replica3,
		},
	}}, ":8001")
	go RunNewServer(&Controller{&Service{
		storage: NewStorage(),
		replications: []ReplicationInterface{
			replica1,
			replica2,
		},
	}}, ":8002")
	go RunNewServer(&Controller{&Service{
		storage: NewStorage(),
		replications: []ReplicationInterface{
			replica1,
			replica2,
		},
	}}, ":8003")

	replications := []ReplicationInterface{
		replica1,
		replica2,
		replica3,
	}

	service := Service{
		storage:      NewStorage(),
		replications: replications,
	}

	// Test updating without replication.
	fmt.Println("Test updating without replication.")

	fmt.Println("Set testname with testvalue.")
	err := service.Update("testname", "testvalue", false)
	if err != nil {
		t.Fatal(err)
	}

	fmt.Println("Waiting for 1 second.")
	time.Sleep(1 * time.Second)

	fmt.Println("Check if replications don't have testname.")
	for _, replication := range replications {
		_, err := replication.Get("testname")
		if err == nil {
			fmt.Println(1)
			t.Fail()
		}
	}

	// Test updating with replication.
	fmt.Println("Test updating without replication.")

	fmt.Println("Set testname with testvalue.")
	err = service.Update("testname", "testvalue", true)
	if err != nil {
		fmt.Println(2)
		t.Fatal(err)
	}

	fmt.Println("Waiting for 1 second.")
	time.Sleep(1 * time.Second)

	fmt.Println("Check if replications have testname with testvalue.")
	for _, replication := range replications {
		r, err := replication.Get("testname")
		if err != nil || r != "testvalue" {
			fmt.Println(3)
			t.Fail()
		}
	}

	// Test removing with replication.
	fmt.Println("Test removing with replication.")
	err = service.Remove("testname", true)
	if err != nil {
		t.Fatal(err)
	}

	fmt.Println("Waiting for 1 second.")
	time.Sleep(1 * time.Second)

	fmt.Println("Check if replications doesn't have testname.")
	for _, replication := range replications {
		_, err := replication.Get("testname")
		if err == nil {
			fmt.Println(4)
			t.Fail()
		}
	}
}
