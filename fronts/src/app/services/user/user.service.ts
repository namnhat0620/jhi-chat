import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'src/app/core/config/application-config.service';
import { StringConstants } from 'src/app/config/constants';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly apiUrl =
    this.applicationConfigService.getEndpointFor('users');

  constructor(
    private readonly http: HttpClient,
    private readonly applicationConfigService: ApplicationConfigService
  ) { }

  getUserByUsername(username: string): Observable<any> {
    return this.http.get(`${this.apiUrl}?login.contains=${this.getSearchParam(username)}`);
  }

  private getSearchParam(search: string): string {
    return `${StringConstants.PERCENT}${search}${StringConstants.PERCENT}`
  }
}
