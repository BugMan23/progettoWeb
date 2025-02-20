import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import {routes} from './app/app.routes';
import {importProvidersFrom} from '@angular/core';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    importProvidersFrom(RouterModule.forRoot(routes))
  ]
});


bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient()
  ]
}).catch(err => console.error(err));
