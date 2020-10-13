import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { apiUrl } from '../../../environments/environment'
import { WeekDays } from 'src/app/shared/enums/WeekDays.enum'
import { WorkoutDay } from 'src/app/shared/interfaces/WorkoutDay'
import { WorkoutDayExercise } from 'src/app/shared/interfaces/WorkoutDayExercise'

@Injectable({
  providedIn: 'root',
})
export class WorkoutDaysService {
  constructor(private http: HttpClient) {}

  getWorkoutDay(wkPlanId: number, dayOfWeek: WeekDays): Observable<WorkoutDayExercise[]> {
    const URL = `${apiUrl.workoutDay}/${wkPlanId}/${WeekDays[dayOfWeek]}`

    return this.http.get<WorkoutDayExercise[]>(URL)
  }
}
