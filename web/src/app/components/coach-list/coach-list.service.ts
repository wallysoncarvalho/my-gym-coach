import { Injectable } from '@angular/core'
import { apiUrl } from '../../../environments/environment'
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs'

@Injectable({
  providedIn: 'root',
})
export class CoachListService {
  constructor(private http: HttpClient) {}

  getCoachesAvailableForUser(athleteId: string): Observable<CoachInformation[]> {
    const URL = `${apiUrl.athlete}/${athleteId}/disconnected-coaches`
    return this.http.get<CoachInformation[]>(URL)
  }

  createWorkoutPlan(athleteId: string, coachId: string): Observable<WorkoutPlan> {
    const URL = `${apiUrl.workout}`
    return this.http.post<WorkoutPlan>(URL, { athleteId, coachId })
  }

  cancelPendingWorkoutPlan(athleteId: string, coachId: string): Observable<WorkoutPlan> {
    const URL = `${apiUrl.workout}/cancel`
    return this.http.post<WorkoutPlan>(URL, { athleteId, coachId })
  }
}
