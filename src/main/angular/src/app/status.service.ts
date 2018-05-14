import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StatusService {
  constructor() { }
  getStatus() {
    
    return {connected:false, trying_to_connect:true, port:"COM6"}
  }
}
