package main

import (
	"net/http"
	"fmt"
	"io/ioutil"
)

var path = "../angular/dist/angular/"

func main() {
	http.HandleFunc("/", handler)
	http.ListenAndServe("127.0.0.1:8000", nil)
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
			data = []byte("{status:{connected:false}}")
		default:
			data, err = ioutil.ReadFile(path + r.URL.Path[1:])
		}
		if err != nil {
			w.WriteHeader(500)
			fmt.Fprint(w, "500 - Something went wrong!")
		} else {
			w.Write(data)
		}

	case "POST":
		fmt.Println("kult")
	default:
		w.WriteHeader(http.StatusBadRequest)
	}
}