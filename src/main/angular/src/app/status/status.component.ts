import { StatusService } from '../status.service'
import { Component, OnInit } from '@angular/core';
import { Status } from '../status.service';
import { $ } from 'protractor';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {
  title = "Status";
  connected;
  knownport = true;

  constructor(service: StatusService) {
    service.getStatus().subscribe(
     (data: Status) => this.setupData(data),
     error => console.log(error)
    );
  }

  private setupData(data: Status) {
    if (data.connected) {
      this.connected = "Connected on " + data.port;
    } else if(data.trying_to_connect) {
      this.connected = "Trying to connect on " + data.port + "...";
    } else {
      this.connected = "Not connected.";
    }
    this.knownport = data.known_port;
    if (this.knownport) {
    
    }
  }

  
  ngOnInit() {
  }

}
