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
)

var stop = false
var conn net.Conn = nil

func main() {

	fmt.Println("Launching server...")
	tcpReady := make(chan bool)
	go tcpConnect(tcpReady)
	go sendData(tcpReady)
	feedBack(tcpReady)
}

func tcpConnect(tcpReady chan bool) {
	// listen on all interfaces
	ln, _ := net.Listen("tcp", ":9001")

	// accept connection on port

	conn, _ = ln.Accept()

	// run loop forever (or until ctrl-c)

	// will listen for message to process ending in newline (\n)
	//message, err := bufio.NewReader(conn).ReadString('\n')
	//if err != nil {
	//	os.Exit(-1)
	//}
	tcpReady <- true
	tcpReady <- true
	// output message received
	fmt.Print("Message Received:")
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
	adaptor, spheroDriver := getSphero()
	spheroDriver.SetStabilization(false)

	work := func() {
		fmt.Printf("%d\n", os.Getpid())

		spheroDriver.SetDataStreaming(sphero.DefaultDataStreamingConfig())
		<-tcpReady
		fmt.Println("2")
		spheroDriver.On(sphero.SensorData, func(data interface{}) {
			if stop {
				os.Exit(1);
			}

			var roll int16 = data.(sphero.DataStreamingPacket).FiltRoll
			var pitch int16 = data.(sphero.DataStreamingPacket).FiltPitch
			var yaw int16 = data.(sphero.DataStreamingPacket).FiltYaw
			d := strconv.Itoa(int(roll)) + " " + strconv.Itoa(int(pitch)) + " " + strconv.Itoa(int(yaw)) + "\n"
			fmt.Print(d)
			conn.Write([]byte(d))

		})
	}

	robot := gobot.NewRobot("sphero",
		[]gobot.Connection{adaptor},
		[]gobot.Device{spheroDriver},
		work,
	)

	robot.Start()

}

func feedBack(tcpReady chan bool) {
	<-tcpReady
	fmt.Println("2")
	for !stop {
		_, err := bufio.NewReader(conn).ReadString('\n')
		if err != nil {
			stop = true
			fmt.Println("Error received:", err.Error())
		}
	}
}
