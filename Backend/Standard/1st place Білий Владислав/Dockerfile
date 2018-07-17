FROM golang:1.10.2

WORKDIR /go/src/final
COPY . .

RUN go get -d -v ./...
RUN go install -v ./...

CMD ["final"]