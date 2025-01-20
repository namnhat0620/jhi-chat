import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { KeycloakService } from './services/keycloak/keycloak.service';
import { HomeComponent } from './home/home.component';
import { CardComponent } from './card/card.component';
import { ListConversationComponent } from './list-conversation/list-conversation.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

// NgPrime
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { HttpTokenInterceptor } from './services/interceptor/http-tocken.interceptor';
import { authGuard } from './services/guard/auth.guard';
import { ConversationComponent } from './conversation/conversation.component';
import { ChatComponent } from './chat/chat.component';
import { MessageSenderComponent } from './message-sender/message-sender.component';

export function kcFactory(keycloakService: KeycloakService) {
  return () => keycloakService.init();
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CardComponent,
    ListConversationComponent,
    ConversationComponent,
    ChatComponent,
    MessageSenderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    InputTextModule,
    ButtonModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpTokenInterceptor,
      multi: true,
    },
    {
      provide: APP_INITIALIZER,
      deps: [KeycloakService],
      useFactory: kcFactory,
      multi: true,
    }
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
