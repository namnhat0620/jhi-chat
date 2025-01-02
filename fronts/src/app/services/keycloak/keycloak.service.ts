import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js'
import { UserProfile } from 'src/app/model/user-profile';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak?: Keycloak;
  private profile?: UserProfile;

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9080',
        realm: 'jhipster',
        clientId: 'web_app'
      })
    }
    return this._keycloak;
  }

  constructor() { }

  async init() {
    const authenticated = await this.keycloak?.init({
      onLoad: 'login-required'
    })
    if (authenticated) {
      this.profile = await this.keycloak.loadUserProfile() as UserProfile;
      this.profile.token = this.keycloak.token;

      this.startTokenRefresh();

    }
  }

  login() {
    return this.keycloak.login()
  }

  logout() {
    // return this.keycloak.logout({redirectUri: 'http://localhost:4200'})
  }

  private startTokenRefresh() {
    // Refresh token before it expires (typically refresh token is valid longer than access token)
    setInterval(async () => {
      if (this.keycloak?.token) {
        const tokenExpirationTime = this.keycloak?.tokenParsed?.exp;
        const currentTime = Math.floor(new Date().getTime() / 1000);  // Current time in seconds
        const remainingTime = (tokenExpirationTime ?? 0) - currentTime;

        if (remainingTime < 60) {  // Token expires in less than 60 seconds
          try {
            await this.keycloak?.updateToken(30);  // Refresh token if it will expire within 30 seconds
            console.log('Token refreshed successfully');
          } catch (error) {
            console.error('Error refreshing token', error);
          }
        }
      }
    }, 30000); // Check every 30 seconds if the token needs to be refreshed
  }
}
