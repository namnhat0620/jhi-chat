import { Component, OnInit } from '@angular/core';
import { Group } from '../services/group/group.model';
import { KeycloakService } from '../services/keycloak/keycloak.service';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  group: Group | null = null;
  socketClient: any = null;

  constructor(
    private readonly keycloakService: KeycloakService
  ) { }

  ngOnInit(): void {
    this.initWebSocket();
  }

  handleGroupChange(group: Group) {
    this.group = group;
  }

  private initWebSocket() {
    if (this.keycloakService.keycloak.tokenParsed?.sub) {
      let ws = new SockJS('http://localhost:8083/ws');
      this.socketClient = Stomp.over(ws);
      const subUrl = `user/${this.keycloakService.keycloak.tokenParsed?.sub}/chat`;
      this.socketClient.connect({ Authorization: `Bearer ${this.keycloakService.keycloak.token}` }, () => {
        this.socketClient.subscribe(subUrl, (message: any) => {
          console.log("Received message", message);
        }, () => console.error('Error subscribing to ' + subUrl));
      });
    }
  }
}
