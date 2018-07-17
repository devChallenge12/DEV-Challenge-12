import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { NgModule, ApplicationRef } from '@angular/core';
import { MatDialog,
         MatDialogRef,
         MatSnackBar,
         MatSnackBarModule,
         MatMenuModule,
         MatButtonModule,
         MatSidenavModule,
         MatSelectModule,
         MatInputModule,
         MatIconModule,
         MatSlideToggleModule,
         MatDividerModule,
         MatTabsModule,
         MatTableModule,
         MatPaginatorModule,
         MatSortModule,
         MatDialogModule,
         MatStepperModule,
         MatDatepickerModule,
         MatProgressBarModule } from '@angular/material';
import { AppComponent } from './app.component';
import 'hammerjs';
import * as THREE from 'three';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    MatIconModule,
    MatButtonModule,
    MatProgressBarModule,
    MatMenuModule,
    MatSidenavModule,
    MatSelectModule,
    MatInputModule,
    MatSlideToggleModule,
    MatDividerModule,
    MatSnackBarModule,
    MatTabsModule,
    MatTableModule,
    MatDialogModule,
    MatPaginatorModule,
    MatSortModule,
    MatStepperModule,
    MatDatepickerModule
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {}
