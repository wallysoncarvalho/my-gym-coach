import { Component, OnInit } from '@angular/core'
import { WorkoutDaysService } from './workout-days.service'
import { WeekDays } from 'src/app/shared/enums/WeekDays.enum'
import { WorkoutDayExercise } from 'src/app/shared/interfaces/WorkoutDayExercise'

@Component({
  selector: 'app-workout-days',
  templateUrl: './workout-days.component.html',
  styleUrls: ['./workout-days.component.scss'],
})
export class WorkoutDaysComponent implements OnInit {
  private wkPlanId = 50
  accordionDay: number

  monday: WorkoutDayExercise[] = []
  tuesday: WorkoutDayExercise[] = []
  wednesday: WorkoutDayExercise[] = []

  constructor(private wkDayService: WorkoutDaysService) {}

  ngOnInit(): void {
    const currentWeekDay = new Date().getDay()
    const accDay = currentWeekDay === 0 ? 1 : currentWeekDay - 1
    this.accordionDay = accDay
    const event = { index: accDay }
    this.getWorkoutDayExercises(event)
  }

  getWorkoutDayExercises(event): void {
    const weekDay = this.getDayFromAccordionIndex(event.index)
    this.setDayExercises(weekDay)
  }

  private getDayFromAccordionIndex(index: number): WeekDays {
    let day = WeekDays.MONDAY
    if (index === 1) {
      day = WeekDays.TUESDAY
    } else if (index === 2) {
      day = WeekDays.WEDNESDAY
    } else if (index === 3) {
      day = WeekDays.THURSDAY
    }
    return day
  }

  private setDayExercises(weekDay: WeekDays): void {
    const workouts = this.wkDayService.getWorkoutDay(this.wkPlanId, weekDay)

    if (weekDay === WeekDays.MONDAY && this.monday.length === 0) {
      workouts.subscribe((wk) => this.monday.push(...wk))
    } else if (weekDay === WeekDays.TUESDAY && this.tuesday.length === 0) {
      workouts.subscribe((wk) => this.tuesday.push(...wk))
    } else if (weekDay === WeekDays.WEDNESDAY && this.wednesday.length === 0) {
      workouts.subscribe((wk) => this.wednesday.push(...wk))
    }
  }
}
