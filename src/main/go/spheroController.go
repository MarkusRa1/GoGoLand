package main

import (
	"fmt"
	"time"

	"gobot.io/x/gobot"
	"gobot.io/x/gobot/platforms/sphero"
	"runtime"
	"os/exec"
	"strings"
)


func main() {
	var roll int16 = 0
	var pitch int16 = 0
	var yaw int16 = 0

	adaptor, spheroDriver := getSphero()
	spheroDriver.SetStabilization(false)

	work := func() {
		spheroDriver.SetDataStreaming(sphero.DefaultDataStreamingConfig())

		spheroDriver.On(sphero.SensorData, func(data interface{}) {
			roll = data.(sphero.DataStreamingPacket).FiltRoll;
			pitch = data.(sphero.DataStreamingPacket).FiltPitch;
			yaw = data.(sphero.DataStreamingPacket).FiltYaw;
		})
	}

	robot := gobot.NewRobot("sphero",
		[]gobot.Connection{adaptor},
		[]gobot.Device{spheroDriver},
		work,
	)

	ticker := time.NewTicker(100000000 * time.Nanosecond)
	quit := make(chan struct{})
	go func() {
		for {
			select {
			case <-ticker.C:
				fmt.Printf("%d %d %d\n", roll, pitch, yaw)
			case <-quit:
				ticker.Stop()
				return
			}
		}
	}()

	robot.Start()

}
func getSphero() (*sphero.Adaptor, *sphero.SpheroDriver) {
	var adaptor *sphero.Adaptor
	switch runtime.GOOS {
	case "windows":
		adaptor = sphero.NewAdaptor("COM3")
	case "darwin":
		op, _ := exec.Command("/bin/sh", "./findspheromac.sh").Output()
		adaptor = sphero.NewAdaptor("/dev/" + strings.TrimRight(string(op), "\n"))
	default:
		fmt.Println("OS not supported yet...")
		adaptor = nil
	}
	spheroDriver := sphero.NewSpheroDriver(adaptor)
	return adaptor, spheroDriver
}