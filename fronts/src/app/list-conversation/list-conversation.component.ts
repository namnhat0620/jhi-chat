import { Component, EventEmitter, Output } from '@angular/core';
import { debounceTime, distinctUntilChanged, Subject, switchMap } from 'rxjs';
import { UserService } from '../services/user/user.service';
import { Group } from '../services/group/group.model';

@Component({
  selector: 'app-list-conversation',
  templateUrl: './list-conversation.component.html',
  styleUrls: ['./list-conversation.component.scss'],
})
export class ListConversationComponent {
  value = ''; // Value from the input field
  isValueExisted = false;
  conversations: any[] = [];
  private valueChanged = new Subject<string>(); // Subject to emit value changes
  @Output() groupChangeEvent = new EventEmitter<Group>();

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    // Subscribe to the valueChanged subject in ngOnInit to handle API calls
    this.valueChanged
      .pipe(
        debounceTime(300), // Wait for 1000ms after last change
        distinctUntilChanged(), // Emit only when the value changes
        switchMap((value: string) => this.userService.getUserByUsername(value)) // API call
      )
      .subscribe({
        next: (data) => this.conversations = data, // Handle API response
        error: (data) => console.error(data)
      });
  }

  // This method will be called whenever the value in the input field changes
  onValueChange(): void {
    this.valueChanged.next(this.value); // Emit the current value to trigger the API call
    this.isValueExisted = !!this.value
  }

  clearValue(): void {
    this.value = ""
    this.onValueChange()
  }

  handleGroupChange(group: Group | null) {
    if(!group) return;
    this.groupChangeEvent.emit(group)
  }
}
