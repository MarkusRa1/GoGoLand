import { StatusService } from '../status.service'
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {
  title = "Status";
  connected;

  constructor(service: StatusService) {
    let stats = service.getStatus();
    if (stats.connected) {
      this.connected = "Connected on " + stats.port;
    } else if(stats.tryingToConnect){
      this.connected = "Trying to connect on " + stats.port + "...";
    } else {
      this.connected = "Not connected.";
    }
  }

  ngOnInit() {
  }

}
