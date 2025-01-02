import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './services/guard/auth.guard';
import { HomeComponent } from './home/home.component';

const routes: Routes = [{
  path: '',
  canActivate: [authGuard],  // Apply the guard here for all child routes
  children: [
    { path: '', component: HomeComponent },
  ]
}];

@NgModule({
  imports: [RouterModule.forRoot(routes,  {
    initialNavigation: isPlatformBrowserCustom() ? 'enabledNonBlocking' : 'enabledBlocking'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

export function isPlatformBrowserCustom() {
  return typeof window !== 'undefined';
}
