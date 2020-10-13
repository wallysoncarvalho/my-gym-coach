import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable, throwError, of } from 'rxjs'
import { catchError, retry, tap, map, takeUntil } from 'rxjs/operators'
import { MessageService } from 'primeng/api'
import { ToastMsg } from './classes/ToastMsg'
import { ToastSeverity } from './enums/ToastSeverity.enum'
import { AppService } from '../app.service'
import { Router, ActivationEnd } from '@angular/router'

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private toast: MessageService, private service: AppService, router: Router) {
    // Listens to route changes and cancel ongoing requests...
    router.events.subscribe((event) => {
      if (event instanceof ActivationEnd) {
        this.service.cancelPendingRequests()
        AppService.showProgressBar.emit(false)
      }
    })
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    AppService.showProgressBar.emit(true)
    const makingRequestMessage: BlockuiMessage = {
      blockui: true,
      message: 'Fetching data',
      showCloseButton: false,
    }

    return next.handle(req).pipe(
      map((event: HttpEvent<any>) => {
        event instanceof HttpResponse && event.ok && AppService.showProgressBar.emit(false)
        return event
      }),
      takeUntil(this.service.onCancelPendingRequests()),
      catchError((error: HttpErrorResponse) => {
        let errorMsg: string

        if (error.status === 0) {
          errorMsg = `Couldn't connect to the following address ${error.url}`
          this.generateErrorMessageAndBlockui(errorMsg)
        } else if (error.status === 500) {
          errorMsg = 'An error ocurred to the server !'
          this.generateErrorMessageAndBlockui(errorMsg)
        } else if (error.status >= 400 && error.status <= 500) {
          const apiResponse: ApiResponse<any> = error.error
          const toastErrorMsg = ToastMsg.build(
            ToastSeverity.WARN,
            'Something went wrong 💩 !',
            apiResponse.message
          )
          this.toast.add(toastErrorMsg)
        }

        AppService.showProgressBar.emit(false)

        return of(null)
      })
    )
  }

  private generateErrorMessageAndBlockui(msg) {
    const requestErrorMessage: BlockuiMessage = {
      blockui: true,
      message: `Connection error: ${msg}`,
      imgUrl: 'assets/images/failure.svg',
    }

    AppService.blockui.emit(requestErrorMessage)
  }
}
