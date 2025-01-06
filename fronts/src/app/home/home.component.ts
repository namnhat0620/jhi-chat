import { Component } from '@angular/core';
import { Group } from '../services/group/group.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  group: Group | null = null;

  handleGroupChange(group: Group) {
    this.group = group;
  }
}
