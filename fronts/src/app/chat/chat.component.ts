import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Group } from '../services/group/group.model';
import { GroupService } from '../services/group/group.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnChanges {
  @Input() group: Group | null = null;

  constructor(private readonly groupService: GroupService) { }

  ngOnChanges(changes: SimpleChanges): void {
  }
}
