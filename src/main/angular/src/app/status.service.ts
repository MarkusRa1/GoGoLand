import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Status {
  connected: boolean;
  trying_to_connect: boolean;
  port: string;
  is_monitoring: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class StatusService {
  statusUrl = "assets/status.json"

  constructor(private http: HttpClient) { }
  getStatus() {
    return this.http.get<Status>(this.statusUrl);
  }
}
