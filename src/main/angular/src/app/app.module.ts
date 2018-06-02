import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { StatusComponent } from './status/status.component';

import { HttpClientModule } from '@angular/common/http';
import { PortFormComponent } from './port-form/port-form.component';

@NgModule({
  declarations: [
    AppComponent,
    StatusComponent,
    PortFormComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [
    //StatusService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
