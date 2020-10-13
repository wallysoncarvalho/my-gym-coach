import { Injectable, EventEmitter } from '@angular/core'
import { Subject } from 'rxjs'
import { HttpClient } from '@angular/common/http'
import { apiUrl } from '../environments/environment'

@Injectable({
  providedIn: 'root',
})
export class AppService {
  static blockui = new EventEmitter<BlockuiMessage>()
  static showProgressBar = new EventEmitter<boolean>()
  private pendingHTTPRequests = new Subject<void>()

  constructor(private http: HttpClient) {}

  public cancelPendingRequests() {
    this.pendingHTTPRequests.next()
  }

  public onCancelPendingRequests() {
    return this.pendingHTTPRequests.asObservable()
  }

}
