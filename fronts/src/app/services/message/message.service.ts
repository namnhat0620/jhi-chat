import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Message } from './message.model';
import { ApplicationConfigService } from 'src/app/core/config/application-config.service';
import { createRequestOption } from 'src/app/core/request/request-util';

export type EntityResponseType = HttpResponse<Message>;
export type EntityArrayResponseType = HttpResponse<Message[]>;

@Injectable({ providedIn: 'root' })
export class MessageService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/messages', 'message');

  create(message: Message): Observable<EntityResponseType> {
    return this.http.post<Message>(this.resourceUrl, message, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<Message>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<Message[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAll(): Observable<EntityArrayResponseType> {
    return this.http.get<Message[]>(this.resourceUrl, { observe: 'response' });
  }
}
