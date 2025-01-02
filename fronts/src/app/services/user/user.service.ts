import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'src/app/core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly apiUrl =
    this.applicationConfigService.getEndpointFor('users');

  constructor(
    private readonly http: HttpClient,
    private readonly applicationConfigService: ApplicationConfigService
  ) {}

  getUserByUsername(username: string): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }
}
