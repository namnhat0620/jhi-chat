import { Component, Input } from '@angular/core';
import { Message } from '../services/message/message.model';

@Component({
  selector: 'app-message-text-content',
  templateUrl: './message-text-content.component.html',
  styleUrls: ['./message-text-content.component.scss']
})
export class MessageTextContentComponent {
  @Input() message: Message | null = null;
}
