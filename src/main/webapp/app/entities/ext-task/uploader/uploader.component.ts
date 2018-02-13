import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'jhi-uploader',
  templateUrl: './uploader.component.html',
  styles: []
})
export class UploaderComponent implements OnInit {

  @Input() source: string;
  @Input() id: number;
  @Input() title: string;

  constructor() { }

  ngOnInit() {
  }

}
