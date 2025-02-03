import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MessageService } from '../services/message/message.service';
import { Group } from '../services/group/group.model';
import { Message, MessageType } from '../services/message/message.model';
import { KeycloakService } from '../services/keycloak/keycloak.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-message-sender',
  templateUrl: './message-sender.component.html',
  styleUrls: ['./message-sender.component.scss'],
})
export class MessageSenderComponent {
  message: string = '';
  @Input() group: Group | null = null;
  @Output() messageSent = new EventEmitter<Message>();

  constructor(
    private readonly messageService: MessageService,
    private readonly keycloakService: KeycloakService
  ) { }

  sendMessage() {
    this.messageService.create(this.buildMessage()).subscribe((res: HttpResponse<Message>) => {
      if (res?.body) {
        this.messageSent.emit(res.body);
        this.message = '';
      }
    });
  }

  private buildMessage(): Message {
    return {
      sentToLogin: this.group?.userGroups?.find(
        (userGroup) => userGroup?.login !== this.keycloakService?.currentLogin
      )?.login,
      content: this.message,
      messageType: MessageType.TEXT,
      groupId: this.group?.id,
    };
  }
}
