import { Component, EventEmitter, Inject, Input, OnChanges, OnInit, Output } from '@angular/core';
import { Group, GroupType } from '../services/group/group.model';
import { GroupService } from '../services/group/group.service';

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.scss']
})
export class ConversationComponent implements OnChanges {
  name: string = '';
  avatar: string = 'assets/avatar-default.svg';

  @Input() conversation: any;

  @Input() type: 'USER' | 'CONVERSATION' = 'CONVERSATION';
  @Output() groupChangeEvent = new EventEmitter<Group | null>();

  constructor(private readonly groupService: GroupService) { }

  ngOnChanges(): void {
    this.name = (this.type === 'USER' ? this.conversation?.login : this.conversation?.name) ?? '';
    this.avatar = this.conversation?.avatar ?? 'assets/avatar-default.svg';
  }

  createConversation() {
    if (this.type === 'CONVERSATION') {
      return;
    }
    this.groupService.create({
      type: GroupType.PRIVATE,
      userGroups: [{
        login: this.name
      }]
    }).subscribe((res) => {
      this.groupChangeEvent.emit(res?.body)
    })
  }
}
