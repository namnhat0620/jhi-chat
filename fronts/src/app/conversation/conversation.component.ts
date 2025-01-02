import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.scss']
})
export class ConversationComponent {
  private _conversation: any = null;
  userName: string = '';
  avatarUrl: string = 'assets/avatar-default.svg';

  @Input() set conversation(value: any) {
    this._conversation = value;
    this.userName = value?.login ?? '';
    this.avatarUrl = value?.avatar ?? 'assets/avatar-default.svg';
  }

  get conversation(): any {
    return this._conversation;
  }
}
