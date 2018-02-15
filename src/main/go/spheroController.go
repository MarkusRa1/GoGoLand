package main

import (
	"fmt"
	"time"

	"gobot.io/x/gobot"
	"gobot.io/x/gobot/platforms/sphero"
	"runtime"
	"os/exec"
	"strings"
	"math"
)


func main() {
	var roll float64 = 0
	var pitch float64 = 0
	var yaw float64 = 0
	var x float64 = 0
	var y float64 = 0
	var z float64 = 0

	adaptor, spheroDriver := getSphero()
	spheroDriver.SetStabilization(false)

	work := func() {
		spheroDriver.SetDataStreaming(sphero.DefaultDataStreamingConfig())

		spheroDriver.On(sphero.SensorData, func(data interface{}) {
			roll = float64(data.(sphero.DataStreamingPacket).FiltRoll) / 180 * math.Pi;
			pitch = float64(data.(sphero.DataStreamingPacket).FiltPitch) / 180 * math.Pi;
			yaw = float64(data.(sphero.DataStreamingPacket).FiltYaw) / 180 * math.Pi;
			x = -math.Sin(roll)*math.Cos(yaw)-math.Cos(roll)*math.Sin(pitch)*math.Sin(yaw)
			y = math.Sin(roll)*math.Sin(yaw)-math.Cos(roll)*math.Sin(pitch)*math.Cos(yaw)
			z = math.Cos(roll)*math.Cos(pitch)
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
				fmt.Printf("%f %f %f\n", x, y, z)
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