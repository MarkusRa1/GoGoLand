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
)

var stop = false
var conn net.Conn = nil
var spheroConnectedOrTrying = false
var port = "9001"
var dontCloseWhenJavaClose = true
var lostConnection = false
var ln net.Listener = nil
var tcpConnected = false
var robot *gobot.Robot = nil
var incomingCommand = make(chan SpheroCommand)
var stopSpheroConnection = make(chan bool)
var isMonitoring = false
var reConnect = false
var readyToRestartSpheroConnection = make(chan bool)

type SpheroCommand struct {
	name string
	value int
}

func main() {
	if len(os.Args) > 1 {
		port = os.Args[1]
		dontCloseWhenJavaClose = false
	}
	
	fmt.Println("Launching server...")

	go sendData()
	tcpConnect()
}

func tcpConnect() {
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
	tcpConnected = true
	fmt.Println("Connection setup")
	feedBack()
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

func sendData() {
	reConnect = true
	var stopS = false
	for reConnect && !stop && !stopS {
		reConnect = false;
		spheroConnectedOrTrying = true;
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
				if !lostConnection && tcpConnected && !stop {
					conn.Write([]byte(d))
				}
				//fmt.Print(d)
				if !isMonitoring {
					isMonitoring = true
				}
			})

			for !stop && !reConnect {
				var c SpheroCommand
				c = <-incomingCommand
				switch c.name {
				case "Connect":
					reConnect = true
					stopSpheroConnection<-true
					break
				case "Stop":
					stop = true
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
		var tryToConnect bool = true
		for tryToConnect && !stop {
			sErr := robot.Start()
			if sErr == nil {
				tryToConnect = false
			}
		}

		<-stopSpheroConnection
		robot.Stop()
		adaptor.Disconnect()
		if reConnect {
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
		message, err := bufio.NewReader(conn).ReadString('\n')
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
			fmt.Println("sweet connect")
			if spheroConnectedOrTrying {
				incomingCommand<-SpheroCommand{"Connect", 0}
				<-readyToRestartSpheroConnection
				fmt.Println("hmm")
			}
			go sendData()
		}
	}
}

