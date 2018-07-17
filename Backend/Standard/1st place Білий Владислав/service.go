package main

import (
	"errors"
	"fmt"
	"log"
	"os"

	"github.com/satori/go.uuid"
)

type ServiceInterface interface {
	Update(name string, value string, replicate bool) error
	Get(name string) (string, error)
	Remove(name string, replicate bool) error
	Dump() (string, error)
}

type Service struct {
	storage      StorageInterface
	replications []ReplicationInterface
}

func (s *Service) Update(name string, value string, replicate bool) error {
	if !s.storage.Set(name, value) {
		return errors.New(fmt.Sprintf("value %s can't be set to %s", value, name))
	}

	if replicate {
		// Replicate updating to other instances.
		go func() {
			for _, replication := range s.replications {
				err := replication.Update(name, value)
				if err != nil {
					log.Print(err)
				}
			}
		}()
	}

	return nil
}

func (s *Service) Get(name string) (string, error) {
	value, ok := s.storage.Get(name)
	if !ok {
		return "", errors.New(fmt.Sprintf("%s can't be gotten", name))
	}

	return value, nil
}

func (s *Service) Remove(name string, replicate bool) error {
	if !s.storage.Delete(name) {
		return errors.New(fmt.Sprintf("%s can't be deleted", name))
	}

	if replicate {
		// Replicate removing to other instances.
		go func() {
			for _, replication := range s.replications {
				err := replication.Remove(name)
				if err != nil {
					log.Print(err)
				}
			}
		}()
	}

	return nil
}

func (s *Service) Dump() (string, error) {
	uuidName, err := uuid.NewV4()
	path := "/tmp/" + uuidName.String()

	f, err := os.Create(path)
	if err != nil {
		errors.New(fmt.Sprintf("it can't open the file %s", path))
	}
	defer f.Close()

	items, ok := s.storage.GetAll()
	if !ok {
		return "", errors.New("data can't be dumped")
	}

	for name, value := range items {
		_, err := f.WriteString(fmt.Sprintf("%s;%s\n", name, value))
		if err != nil {
			return "", errors.New("data can't be dumped")
		}
	}

	return path, nil
}
