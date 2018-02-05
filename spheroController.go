package main

import (
	"fmt"
	"time"
	_ "math"

	"gobot.io/x/gobot"
	"gobot.io/x/gobot/platforms/sphero"
	"runtime"
)


func main() {
	var roll int16 = 0
	var pitch int16 = 0
	var yaw int16 = 0

	var adaptor *sphero.Adaptor
	if(runtime.GOOS == "windows") {
		adaptor = sphero.NewAdaptor("COM3")
	} else {
		adaptor = sphero.NewAdaptor("/dev/tty.Sphero-GWG-AMP-SPP")
	}
	spheroDriver := sphero.NewSpheroDriver(adaptor)
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

	ticker := time.NewTicker(100000000*time.Nanosecond)
	quit := make(chan struct{})
	go func() {
		for {
			select {
			case <- ticker.C:
				fmt.Printf("%d %d %d\n", roll, pitch, yaw)
			case <- quit:
				ticker.Stop()
				return
			}
		}
	}()

	robot.Start()

}