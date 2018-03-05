package main

import (
	"fmt"

	"gobot.io/x/gobot"
	"gobot.io/x/gobot/platforms/sphero"
	"os"
	_ "os/exec"
	"runtime"
	_ "strings"
)

func main() {
	adaptor, spheroDriver := getSphero()
	spheroDriver.SetStabilization(false)

	work := func() {
		fmt.Printf("%d\n", os.Getpid())

		spheroDriver.SetDataStreaming(sphero.DefaultDataStreamingConfig())

		spheroDriver.On(sphero.SensorData, func(data interface{}) {
			var roll int16 = data.(sphero.DataStreamingPacket).FiltRoll
			var pitch int16 = data.(sphero.DataStreamingPacket).FiltPitch
			var yaw int16 = data.(sphero.DataStreamingPacket).FiltYaw
			fmt.Printf("%d %d %d\n", roll, pitch, yaw)
		})
	}

	robot := gobot.NewRobot("sphero",
		[]gobot.Connection{adaptor},
		[]gobot.Device{spheroDriver},
		work,
	)

	robot.Start()

}
func getSphero() (*sphero.Adaptor, *sphero.SpheroDriver) {
	var adaptor *sphero.Adaptor
	switch runtime.GOOS {
	case "windows":
		adaptor = sphero.NewAdaptor("COM3")
	case "darwin":
		//op, _ := exec.Command("/bin/sh", "./findspheromac.sh").Output()
		adaptor = sphero.NewAdaptor("/dev/" + os.Args[1])
	default:
		fmt.Println("OS not supported yet...")
		adaptor = nil
	}
	spheroDriver := sphero.NewSpheroDriver(adaptor)
	return adaptor, spheroDriver
}
