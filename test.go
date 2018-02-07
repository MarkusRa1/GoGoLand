package main

import (
	"os/exec"
	"fmt"
)

func main() {

	yay, e2 := exec.Command("/bin/sh", "./drit.sh").Output()
	if e2 != nil {
		fmt.Print(string(e2.Error()))
	}

	fmt.Print(string(yay))
}
