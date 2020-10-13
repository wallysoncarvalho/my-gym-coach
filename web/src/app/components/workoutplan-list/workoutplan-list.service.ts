import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable, forkJoin } from 'rxjs'
import { apiUrl } from '../../../environments/environment'

@Injectable({
  providedIn: 'root',
})
export class WorkoutplanListService {
  constructor(private http: HttpClient) {}

  getUsersWkPlansByStatus(userId: string, wkStatus: string): Observable<WorkoutPlan[]> {
    const URL = `${apiUrl.workout}/pending/${userId}/${wkStatus}`
    return this.http.get<WorkoutPlan[]>(URL)
  }

  getAllUsersWkPlans(userId: string): Observable<WorkoutPlan[]> {
    const URL = `${apiUrl.workout}/${userId}`
    return this.http.get<WorkoutPlan[]>(URL)
  }

  changeWkPlanStatus(id: string, wkStatus: string): Observable<WorkoutPlan> {
    const URL = `${apiUrl.workout}/change-wkplan-status`
    return this.http.post<WorkoutPlan>(URL, { id, wkStatus })
  }
}
