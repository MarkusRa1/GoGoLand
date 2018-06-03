import { StatusService } from '../status.service'
import { Component, OnInit } from '@angular/core';
import { Status } from '../status.service';
import { $ } from 'protractor';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {
  title = "Status";
  connected;
  knownport = true;

  constructor(service: StatusService, private http: HttpClient) {
    service.getStatus().subscribe(
      (data: Status) => this.setupData(data),
      error => console.log(error)
    );
    document.getElementById("submitCOMInput").addEventListener("click", function() {
      sendPortNumber(<string> document.getElementById("myCOMInput")["value"])
    })
  }

  private setupData(data: Status) {
    if (data.connected) {
      this.connected = "Connected on " + data.port;
    } else if (data.trying_to_connect) {
      this.connected = "Trying to connect on " + data.port + "...";
    } else {
      this.connected = "Not connected.";
    }
    this.knownport = data.known_port;
    if (this.knownport) {
    }
  }

  sendPortNumber = function(comPort: string) {
    if (!/COM.*/.test(comPort)) {
      comPort = "COM" + comPort;
    }
    console.log(comPort);
    
    this.http.post(window.location.href + "/port", { com_port: comPort })
      .subscribe(res => {
        console.log(res);
      }, err => {
        console.log(err);
      });
  }

  ngOnInit() {
  }
  
}
