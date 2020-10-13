interface WorkoutPlan {
  readonly id?: string
  readonly athleteId?: string
  readonly athleteName?: string

  readonly coachId?: string
  readonly coachName?: string

  wkStatus?: string
  readonly creationDate?: Date
  readonly expirationDate?: Date
  readonly status?: string
}
