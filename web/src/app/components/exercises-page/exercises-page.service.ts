import { Injectable } from '@angular/core'
import { Observable, of } from 'rxjs'
import { Muscle } from 'src/app/shared/interfaces/Muscle'
import { HttpClient } from '@angular/common/http'
import { apiUrl } from '../../../environments/environment'

@Injectable({
  providedIn: 'root',
})
export class ExercisesPageService {
  constructor(private http: HttpClient) {}

  getPaginatedMuscles(): Observable<Muscle[]> {
    // const URL = ``
    // return this.http.get<Muscle[]>('')

    const muscles = [
      {
        id: 1,
        name: 'Quads',
      },
      {
        id: 2,
        name: 'Biceps',
      },
      {
        id: 3,
        name: 'Triceps',
      },
    ]

    return of(muscles)
  }

  getPaginatedExercises(): Observable<Exercise[]> {
    const url = apiUrl.exercise
    return this.http.get<Exercise[]>(url)
  }
}
