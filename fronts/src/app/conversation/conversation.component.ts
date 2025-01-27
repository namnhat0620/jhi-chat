import {
  Component,
  EventEmitter,
  Inject,
  Input,
  OnChanges,
  OnInit,
  Output,
} from '@angular/core';
import { Group, GroupType, UserGroup } from '../services/group/group.model';
import { GroupService } from '../services/group/group.service';
import { KeycloakService } from '../services/keycloak/keycloak.service';

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.scss'],
})
export class ConversationComponent implements OnChanges {
  name: string = '';
  avatar: string = 'assets/avatar-default.svg';

  @Input() conversation: any;

  @Input() type: 'USER' | 'GROUP' = 'GROUP';
  @Output() groupChangeEvent = new EventEmitter<Group | null>();

  constructor(
    private readonly groupService: GroupService,
    private readonly keycloakService: KeycloakService
  ) { }

  ngOnChanges(): void {
    const currentLogin = this.keycloakService.currentLogin;
    this.name =
      (this.type === 'USER'
        ? this.conversation?.login
        : this.conversation?.name ??
        this.conversation?.userGroups?.filter(
          (userGroup: UserGroup) =>
            userGroup.login !== currentLogin
        )?.login) ?? '';
    this.avatar = this.conversation?.avatar ?? 'assets/avatar-default.svg';
  }

  createConversation() {
    if (this.type === 'GROUP') {
      this.groupChangeEvent.emit(this.conversation);
      return;
    }
    this.groupService
      .create({
        type: GroupType.PRIVATE,
        userGroups: [
          {
            login: this.name,
          },
        ],
      })
      .subscribe((res) => {
        this.groupChangeEvent.emit(res?.body);
      });
  }
}
