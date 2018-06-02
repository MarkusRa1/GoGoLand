import { Component, OnInit } from '@angular/core';
import {Port} from '../port'
import { $ } from 'protractor';

@Component({
  selector: 'app-port-form',
  templateUrl: './port-form.component.html',
  styleUrls: ['./port-form.component.css']
})
export class PortFormComponent implements OnInit {

  port = new Port("6");
  submitted = false;


  constructor() {
  }

  onSubmit() {
    this.submitted = false;
  }


  get diagnostic() { return JSON.stringify(this.port); }

  ngOnInit() {
  }

}
