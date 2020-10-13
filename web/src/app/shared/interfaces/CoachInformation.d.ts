interface CoachInformation {
  readonly id: string
  readonly name: string
  readonly athletesCoached?: number
  readonly coachSince: Date
  isWorkoutPending?: boolean
}
