package main

import (
	"bytes"
	"errors"
	"fmt"
	"io/ioutil"
	"net"
	"net/http"
	"os"
	"strings"
)

type ReplicationInterface interface {
	SetDsn(host string, port string)
	Get(name string) (string, error)
	Update(name string, value string) error
	Remove(name string) error
}

type Replication struct {
	dsn    string
	client *http.Client
}

func GetReplicationsFromEnv() ([]ReplicationInterface, error) {
	hosts := os.Getenv(REPLICATIONS_ENV)

	var replications []ReplicationInterface
	if hosts != "" {
		hosts := strings.Split(hosts, ",")

		for _, host := range hosts {
			hostname, port, err := net.SplitHostPort(host)
			if err != nil {
				return nil, err
			}

			replication := &Replication{
				client: &http.Client{},
			}
			replication.SetDsn(hostname, port)

			replications = append(replications, replication)
		}
	}

	return replications, nil
}

func (r *Replication) SetDsn(host string, port string) {
	r.dsn = fmt.Sprintf("http://%s:%s/internal/", host, port)
}

func (r *Replication) Get(name string) (string, error) {
	resp, err := http.Get(r.dsn + name)
	if err != nil {
		return "", err
	}

	if resp.StatusCode == http.StatusNotFound {
		return "", errors.New("Not Found")
	}

	value, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	return string(value), nil
}

func (r *Replication) Update(name string, value string) error {
	req, err := http.NewRequest("PUT", r.dsn+name, bytes.NewBufferString(value))
	if err != nil {
		return err
	}

	resp, err := r.client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	return nil
}

func (r *Replication) Remove(name string) error {
	req, err := http.NewRequest("DELETE", r.dsn+name, nil)
	if err != nil {
		return err
	}

	resp, err := r.client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	return nil
}
