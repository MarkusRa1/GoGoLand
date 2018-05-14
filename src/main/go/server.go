package main

import (
	"net/http"
	"fmt"
	"io/ioutil"
	"encoding/json"
	"os"
)

var path = "../angular/dist/angular/"

var netPort = "9001"

var debugging = true

func main() {
	if len(os.Args) > 1 {
		netPort = os.Args[1]
		dontCloseWhenJavaClose = false
	}

	go sendData()
	fmt.Println(os.Getwd())

	http.HandleFunc("/", handler)
	http.ListenAndServe("127.0.0.1:" + netPort, nil)
}

func handler(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case "GET":
		path := r.URL.Path[1:]
		var data []byte
		var err error
		switch path {
		case "":
			data, err = ioutil.ReadFile(path + "index.html")
		case "status.json":
			data, err = json.Marshal(status)
		default:
			data, err = ioutil.ReadFile(path + r.URL.Path[1:])
		}
		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			fmt.Fprint(w, "500 - Something went wrong! :S")
			if debugging {
				fmt.Println(err.Error())
			}
		} else {
			w.Write(data)
		}

	case "POST":
		fmt.Println("kult")
	default:
		w.WriteHeader(http.StatusBadRequest)
	}
}