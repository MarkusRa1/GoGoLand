import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StatusService {
  constructor() { }
  getStatus() {
    return {connected:false, tryingToConnect:true, port:"COM6"}
  }
}
