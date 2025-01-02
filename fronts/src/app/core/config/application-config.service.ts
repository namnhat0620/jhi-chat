import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ApplicationConfigService {
    getEndpointFor(api: string): string {
        return `${environment.serverApiUrl}/${api}`
    }
}