import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Group } from './group.model';
import { ApplicationConfigService } from 'src/app/core/config/application-config.service';
import { createRequestOption } from 'src/app/core/request/request-util';

export type EntityResponseType = HttpResponse<Group>;
export type EntityArrayResponseType = HttpResponse<Group[]>;

@Injectable({ providedIn: 'root' })
export class GroupService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/groups', 'group');

  create(group: Group): Observable<EntityResponseType> {
    return this.http.post<Group>(this.resourceUrl, group, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<Group>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<Group[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
