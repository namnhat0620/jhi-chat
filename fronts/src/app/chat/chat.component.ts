import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Group } from '../services/group/group.model';
import { GroupService } from '../services/group/group.service';
import { Message } from '../services/message/message.model';
import { MessageService } from '../services/message/message.service';
import { HttpResponse } from '@angular/common/http';
import { KeycloakService } from '../services/keycloak/keycloak.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnChanges {
  @Input() group: Group | null = null;
  messages: Message[] = [];
  currentLogin = this.keycloakService.currentLogin;

  constructor(
    private readonly groupService: GroupService,
    private readonly messageService: MessageService,
    private readonly keycloakService: KeycloakService
  ) { }

  ngOnChanges(): void {
    if (this.group) {
      this.messageService.query({ groupId: this.group.id }).subscribe((res: HttpResponse<Message[]>) => {
        this.messages = res.body || [];
      });
    }
  }

  handleMessageSent(message: Message): void {
    this.messages.unshift(message);
  }
}
