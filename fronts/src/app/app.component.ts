import { Component, inject, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { ApplicationConfigService } from './core/config/application-config.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  private readonly applicationConfigService = inject(ApplicationConfigService);

  constructor(private primengConfig: PrimeNGConfig) {
    this.applicationConfigService.setEndpointPrefix(environment.serverApiUrl);

  }

  ngOnInit() {
    this.primengConfig.ripple = true;
  }

  title = 'fronts';
}
