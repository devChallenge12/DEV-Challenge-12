package main

import "sync"

type StorageInterface interface {
	Set(key string, value string) bool
	Get(key string) (string, bool)
	GetAll() (map[string]string, bool)
	Delete(key string) bool
}

type Storage struct {
	sync.RWMutex
	data map[string]string
}

func NewStorage() StorageInterface {
	return &Storage{
		data: make(map[string]string),
	}
}

func (s *Storage) Set(key string, value string) bool {
	s.Lock()
	defer s.Unlock()

	s.data[key] = value

	return true
}

func (s *Storage) Get(key string) (string, bool) {
	s.Lock()
	defer s.Unlock()

	value, ok := s.data[key]

	return value, ok
}

func (s *Storage) GetAll() (map[string]string, bool) {
	s.Lock()
	defer s.Unlock()

	return s.data, true
}

func (s *Storage) Delete(key string) bool {
	s.Lock()
	defer s.Unlock()

	delete(s.data, key)

	return true
}
