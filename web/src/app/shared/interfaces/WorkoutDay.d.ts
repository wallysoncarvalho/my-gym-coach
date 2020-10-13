import { Train } from './Train'

export interface WorkoutDay {
  id?: number
  dayOfWeek?: string
  wkPlanId?: number
  exercises?: Train[]
}
