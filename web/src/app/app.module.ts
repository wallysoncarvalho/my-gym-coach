import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'
import { ApplicationRef, DoBootstrap, NgModule } from '@angular/core'
import { BrowserModule } from '@angular/platform-browser'
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular'
import { environment } from 'src/environments/environment'
import { AppRoutingModule } from './app-routing.module'
import { AppComponent } from './app.component'
import { HttpErrorInterceptor } from './shared/http-error.interceptor'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { MessageService, ConfirmationService } from 'primeng/api'

import { CpfFormatPipe } from './shared/pipes/cpf-format.pipe'
import { DateFormatPipe } from './shared/pipes/date-format.pipe'

import * as myComponents from './components'
import * as primeNgModules from './prime-ng-modules'
import { ServiceWorkerModule } from '@angular/service-worker'

const keycloakService = new KeycloakService()

@NgModule({
  declarations: [...myComponents.components, AppComponent, CpfFormatPipe, DateFormatPipe],
  imports: [
    ...primeNgModules.modules,
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    KeycloakAngularModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production }),
  ],
  providers: [
    {
      provide: KeycloakService,
      useValue: keycloakService,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true,
    },
    MessageService,
    ConfirmationService,
  ],
  // bootstrap: [AppComponent],
  entryComponents: [AppComponent],
})
export class AppModule implements DoBootstrap {
  ngDoBootstrap(appRef: ApplicationRef): void {
    keycloakService
      .init({ config: environment.keycloak })
      .then(() => {
        appRef.bootstrap(AppComponent)
      })
      .catch((error) => {
        console.error('[ngDoBootstrap] init Keycloak failed', error)
      })
  }
}
