package main

import (
	"net"
	"fmt"
	"bufio"
	"os"
	"gobot.io/x/gobot/platforms/sphero"
	"runtime"
	"strconv"
	"gobot.io/x/gobot"
	_ "strings"
	"strings"
)

var stop = false
var conn net.Conn = nil
var spheroConnectedOrTrying = false
var port string = "9001"
var loop = true
var lostConnection = false
var ln net.Listener = nil

func main() {
	if len(os.Args) > 1 {
		port = os.Args[1]
		loop = false
	}
	
	fmt.Println("Launching server...")
	tcpReady := make(chan bool)
	go tcpConnect(tcpReady)
	go sendData(tcpReady)
	feedBack(tcpReady)
}

func tcpConnect(tcpReady chan bool) {
	if !lostConnection {
		var err1 error = nil
		ln, err1 = net.Listen("tcp", "127.0.0.1:" + port)
		if err1 != nil {
			panic("omg " + err1.Error())
		}

	}

	conn, _ = ln.Accept()
	fmt.Println("Ready for connection")
	message, err2 := bufio.NewReader(conn).ReadString('\n')
	if(err2 != nil) {
		fmt.Println(err2.Error())
		panic("Connection error! :O");
	}
	if strings.Compare(message, "Hello Go Beep Boop\n") != 0 {
		panic("Beep Boop who dis is:" + message)
	}
	conn.Write([]byte("Hello Java Beep Boop\n"))
	if !lostConnection {
		tcpReady <- true
		tcpReady <- true
	}
	fmt.Println("Connection setup")
}

func getSphero() (*sphero.Adaptor, *sphero.SpheroDriver) {
	var adaptor *sphero.Adaptor
	switch runtime.GOOS {
	case "windows":
		adaptor = sphero.NewAdaptor("COM6")
	case "darwin":
		//op, _ := exec.Command("/bin/sh", "./findspheromac.sh").Output()
		adaptor = sphero.NewAdaptor("/dev/" + "tty.Sphero-GWG-AMP-SPP" /*strings.TrimRight(string(op), "\n")*/)
	default:
		fmt.Println("OS not supported yet...")
		adaptor = nil
	}
	spheroDriver := sphero.NewSpheroDriver(adaptor)
	return adaptor, spheroDriver
}

func sendData(tcpReady chan bool) {
	spheroConnectedOrTrying = true;
	adaptor, spheroDriver := getSphero()
	spheroDriver.SetStabilization(false)

	work := func() {
		spheroDriver.SetDataStreaming(sphero.DefaultDataStreamingConfig())
		<-tcpReady
		fmt.Println("Starting to monitor...")
		spheroDriver.On(sphero.SensorData, func(data interface{}) {
			var roll int16 = data.(sphero.DataStreamingPacket).FiltRoll
			var pitch int16 = data.(sphero.DataStreamingPacket).FiltPitch
			var yaw int16 = data.(sphero.DataStreamingPacket).FiltYaw
			d := strconv.Itoa(int(roll)) + " " + strconv.Itoa(int(pitch)) + " " + strconv.Itoa(int(yaw)) + "\n"
			if stop {
				os.Exit(1);
			}
			if !lostConnection {
				conn.Write([]byte(d))
			}
			fmt.Print(d)
		})
	}

	robot := gobot.NewRobot("sphero",
		[]gobot.Connection{adaptor},
		[]gobot.Device{spheroDriver},
		work,
	)

	robot.Start()
	spheroConnectedOrTrying = false
}

func feedBack(tcpReady chan bool) {
	<-tcpReady
	for !stop {
		message, err := bufio.NewReader(conn).ReadString('\n')
		if err != nil {
			if loop {
				lostConnection = true
				fmt.Println("Reconnecting...")
				tcpConnect(tcpReady)
				lostConnection = false;
			} else {
				stop = true
				conn.Close()
				fmt.Println("Error received:", err.Error())
			}
		}

		if strings.Compare(message, "Connect") == 0 && !spheroConnectedOrTrying {
			go sendData(tcpReady)
			tcpReady <- true
		}
	}
}
