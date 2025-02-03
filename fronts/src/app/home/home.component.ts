import { Component, OnDestroy, OnInit } from '@angular/core';
import { Group } from '../services/group/group.model';
import { KeycloakService } from '../services/keycloak/keycloak.service';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { GroupService } from '../services/group/group.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  group: Group | null = null;
  socketClient: any = null;
  conversations: any;
  private notificationSubscription: any;

  constructor(
    private readonly keycloakService: KeycloakService,
    private readonly groupService: GroupService
  ) { }

  ngOnInit(): void {
    this.getListGroup();
    this.initWebSocket();
  }

  ngOnDestroy(): void {
    if (this.socketClient !== null) {
      this.socketClient.disconnect();
      this.notificationSubscription.unsubscribe();
      this.socketClient = null;
    }
  }

  handleGroupChange(group: Group) {
    this.group = group;
  }

  private initWebSocket() {
    if (this.keycloakService.keycloak.tokenParsed?.sub) {
      let ws = new SockJS('http://localhost:8083/ws');
      this.socketClient = Stomp.over(ws);
      const subUrl = `user/${this.keycloakService.currentLogin}/chat`;
      this.socketClient.connect({ 'Authorization': 'Bearer ' + this.keycloakService.keycloak.token }, () => {
        console.log('WebSocket connection established');

        this.notificationSubscription = this.socketClient.subscribe(subUrl, (message: any) => {
          console.log("Received message", JSON.parse(message?.body));
        }, () => console.error('Error subscribing to ' + subUrl));
      }, (error: any) => {
        console.error('WebSocket connection error', error);
      });
    }
  }

  private getListGroup() {
    this.groupService.getAll().subscribe((res) => {
      this.conversations = res.body;
    });
  }
}
