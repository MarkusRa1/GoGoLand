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
	"regexp"
	"time"
)

var stop = false
var spheroConnectedOrTrying = false
var dontCloseWhenJavaClose = true
var lostConnection = false
var tcpConnected = false
var udpConnected = false
var isMonitoring = false
var reConnect = false

var tcpconn net.Conn = nil
var udpconn net.Conn = nil
var ln net.Listener = nil



var robot *gobot.Robot = nil

var incomingCommand = make(chan SpheroCommand)
var stopSpheroConnection = make(chan bool)
var readyToRestartSpheroConnection = make(chan bool)

type SpheroCommand struct {
	name string
	value int
}

type Status struct {
	Connected		bool	`json:"connected"`
	TryingToConnect	bool	`json:"trying_to_connect"`
	Port			string	`json:"port"`				// eg. COM6 if windows
	IsMonitoring	bool	`json:"is_monitoring"`
	Os 				string	`json:"os"`
	KnownPort		bool	`json:"known_port"`
}

var status = &Status{
	false,
	false,
	"",
	false,
	"",
	false,
}

//func main() {
//	if len(os.Args) > 1 {
//		netPort = os.Args[1]
//		dontCloseWhenJavaClose = false
//	}
//
//	go sendData()
//	tcpConnect()
//}

func tcpConnect() {
	if !lostConnection {
		var err1 error = nil
		ln, err1 = net.Listen("tcp", "127.0.0.1:" +netPort)
		if err1 != nil {
			panic(err1.Error())
		}
	}
	fmt.Println("Launching server...")
	tcpconn, _ = ln.Accept()
	fmt.Println("Ready for connection")
	message, err2 := bufio.NewReader(tcpconn).ReadString('\n')
	if(err2 != nil) {
		fmt.Println(err2.Error())
		panic("Connection error! :O");
	}
	if strings.Compare(message, "Hello Go Beep Boop\n") != 0 {
		panic("Beep Boop who dis is: " + message)
	}
	tcpconn.Write([]byte("Hello Java Beep Boop\n"))
	udpconn, _ = net.Dial("udp", "127.0.0.1:" +netPort)
	tcpConnected = true
	fmt.Println("Connection setup")
	lostConnection = false
	feedBack()
}

func getSphero() (*sphero.Adaptor, *sphero.SpheroDriver) {
	var adaptor *sphero.Adaptor
	switch runtime.GOOS {
	case "windows":
		prt := "COM6"
		adaptor = sphero.NewAdaptor(prt)
		status.Port = prt
	case "darwin":
		//op, _ := exec.Command("/bin/sh", "./findspheromac.sh").Output()
		adaptor = sphero.NewAdaptor("/dev/" + "tty.Sphero-GWG-AMP-SPP" /*strings.TrimRight(string(op), "\n")*/)
		status.Port = "tty.Sphero-GWG-AMP-SPP"
		default:
		fmt.Println("OS not supported yet...")
		adaptor = nil
		status.Port = "null"
	}
	spheroDriver := sphero.NewSpheroDriver(adaptor)
	return adaptor, spheroDriver
}

func sendData() {
	reConnect = true
	for reConnect && !stop {
		status.TryingToConnect = true
		reConnect = false
		spheroConnectedOrTrying = true
		adaptor, spheroDriver := getSphero()
		spheroDriver.SetStabilization(false)

		work := func() {
			spheroDriver.SetDataStreaming(sphero.DefaultDataStreamingConfig())
			fmt.Println("Starting to monitor...")
			spheroDriver.On(sphero.SensorData, func(data interface{}) {
				var roll int16 = data.(sphero.DataStreamingPacket).FiltRoll
				var pitch int16 = data.(sphero.DataStreamingPacket).FiltPitch
				var yaw int16 = data.(sphero.DataStreamingPacket).FiltYaw
				d := strconv.Itoa(int(roll)) + " " + strconv.Itoa(int(pitch)) + " " + strconv.Itoa(int(yaw)) + "\n"
				if !lostConnection && udpConnected && !stop {
					udpconn.Write([]byte(d))
					//fmt.Println(d)
				}

				if !isMonitoring {
					isMonitoring = true
					status.IsMonitoring = true
				}
			})



			for !stop && !reConnect {
				var c SpheroCommand
				c = <-incomingCommand
				switch c.name {
				case "Connect":
					reConnect = true
					spheroDriver.DeleteEvent(sphero.SensorData)
					stopSpheroConnection<-true
					break
				case "Stop":

					stop = true
					stopSpheroConnection<-true
					break
				}
			}
		}

		robot = gobot.NewRobot("sphero",
			[]gobot.Connection{adaptor},
			[]gobot.Device{spheroDriver},
			work,
		)
		robot.AutoRun = false
		robot.Commander.Commands()
		var tryToConnect = true
		for tryToConnect && !stop {
			sErr := robot.Start()
			if sErr == nil {
				tryToConnect = false
				status.Connected = true
				status.TryingToConnect = false
			} else {
				time.Sleep(time.Second)
			}
		}

		<-stopSpheroConnection
		robot.Stop()
		adaptor.Disconnect()
		gobot.Connection(adaptor).Finalize()
		status.Connected = false
		if reConnect {
			time.Sleep(time.Second)
			readyToRestartSpheroConnection<-true
		}
	}
	spheroConnectedOrTrying = false
	fmt.Println("Stopped sphero connection")
}

func feedBack() {
	_, ereg := regexp.Compile("\\w+ \\d*")
	if ereg != nil {
		fmt.Println("Regex error " + ereg.Error())
	}
	for !stop {
		message, err := bufio.NewReader(tcpconn).ReadString('\n')
		if err != nil {
			if dontCloseWhenJavaClose {
				lostConnection = true
				tcpConnected = false
				fmt.Println("Reconnecting Application...")
				tcpConnect()
				lostConnection = false;
			} else {
				stop = true
				fmt.Println("Error received:", err.Error())
			}
		}

		fmt.Println(message)
		if strings.Compare(message, "Connect\n") == 0 {
			fmt.Println("Connect")
			os.Exit(100) // Temporary
			if spheroConnectedOrTrying {
				incomingCommand<-SpheroCommand{"Connect", 0}
				<-readyToRestartSpheroConnection
			}
			go sendData()
		}
	}
}

