package main

import (
	"net/http"
	"fmt"
	"io/ioutil"
	"os"
	"encoding/json"
	"strconv"
	"net"
)

var netPort = "9001"

var debugging = true

func main() {
	if len(os.Args) > 1 {
		netPort = os.Args[1]
		dontCloseWhenJavaClose = false
	}

	//go sendData()

	http.HandleFunc("/", handler)
	http.ListenAndServe("127.0.0.1:" + netPort, nil)
}

func handler(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case "GET":
		handleGet(r, w)
	case "POST":
		fmt.Println(r.Body)
	default:
		w.WriteHeader(http.StatusBadRequest)
	}
}

func handleGet(r *http.Request, w http.ResponseWriter) {
	path := r.URL.Path[1:]
	var data []byte
	var err error
	fmt.Println(path)
	switch path {
	case "getstream":
		fmt.Println(r.URL.Query()["port"])
		handleStream(r, w)
	case "status.json":
		fallthrough
	case "assets/status.json":
		data, err = json.Marshal(status)
	case "":
		data, err = ioutil.ReadFile("../angular/dist/angular/index.html")
	default:
		data, err = ioutil.ReadFile("../angular/dist/angular/" + r.URL.Path[1:])
	}
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprint(w, "500 - Something went wrong! :S")
		if debugging {
			fmt.Println(err.Error())
		}
	} else if data == nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprint(w, "500 - Something went wrong! :S")
	} else {
		w.Write(data)
	}
}

func handleStream(request *http.Request, w http.ResponseWriter) {
	ports, ok := request.URL.Query()["port"]
	if ok || len(ports) < 1 {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("400 - Require a value for the port!"))
		return
	}

	_, serr := strconv.Atoi(ports[0])
	if serr != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("400 - Port has to be a number!"))
		return
	}

	udpconn, _ = net.Dial("udp", "127.0.0.1:" + ports[0])
	udpConnected = true
}