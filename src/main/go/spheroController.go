package main

import (
	_ "os/exec"
	_ "strings"
	"time"
	"fmt"
)

//func getSphero() (*sphero.Adaptor, *sphero.SpheroDriver) {
//	var adaptor *sphero.Adaptor
//	switch runtime.GOOS {
//	case "windows":
//		adaptor = sphero.NewAdaptor("COM6")
//	case "darwin":
//		//op, _ := exec.Command("/bin/sh", "./findspheromac.sh").Output()
//		adaptor = sphero.NewAdaptor("/dev/" + "tty.Sphero-GWG-AMP-SPP" /*strings.TrimRight(string(op), "\n")*/)
//	default:
//		fmt.Println("OS not supported yet...")
//		adaptor = nil
//	}
//	spheroDriver := sphero.NewSpheroDriver(adaptor)
//	return adaptor, spheroDriver
//}
//
//func sendData(tcpconn net.Conn) {
//	adaptor, spheroDriver := getSphero()
//	if adaptor == nil {
//		tcpconn.Write([]byte("Err 0\n"))
//	}
//	spheroDriver.SetStabilization(false)
//
//	work := func() {
//		fmt.Printf("%d\n", os.Getpid())
//
//		spheroDriver.SetDataStreaming(sphero.DefaultDataStreamingConfig())
//
//		spheroDriver.On(sphero.SensorData, func(data interface{}) {
//			var roll int16 = data.(sphero.DataStreamingPacket).FiltRoll
//			var pitch int16 = data.(sphero.DataStreamingPacket).FiltPitch
//			var yaw int16 = data.(sphero.DataStreamingPacket).FiltYaw
//			d := strconv.Itoa(int(roll)) + " " + strconv.Itoa(int(pitch)) + " " + strconv.Itoa(int(yaw)) + "\n"
//			fmt.Print(d)
//			tcpconn.Write([]byte(d))
//		})
//	}
//
//	robot := gobot.NewRobot("sphero",
//		[]gobot.Connection{adaptor},
//		[]gobot.Device{spheroDriver},
//		work,
//	)
//
//	robot.Start()
//}


func worker(finished chan bool) {
	fmt.Println("Worker: Started")
	time.Sleep(time.Second)
	fmt.Println("Worker: Finished")
	finished <- true
	time.Sleep(time.Second)
	finished <- true
}

func waiter(finished chan bool) {
	<-finished
	fmt.Println("Waiter woke")
}

func main() {
	finished := make(chan bool)

	fmt.Println("Main: Starting worker")
	go waiter(finished)
	go worker(finished)


	fmt.Println("Main: Waiting for worker to finish")
	<- finished
	fmt.Println("Main: Completed")
}


