import { Component, OnInit } from '@angular/core'
import { KeycloakService } from 'keycloak-angular'
import { MessageService } from 'primeng/api'
import { ToastMsg } from 'src/app/shared/classes/ToastMsg'
import { ToastSeverity } from 'src/app/shared/enums/ToastSeverity.enum'
import { CoachListService } from './coach-list.service'
import { AppService } from 'src/app/app.service'

@Component({
  selector: 'app-coach-list',
  templateUrl: './coach-list.component.html',
  styleUrls: ['./coach-list.component.scss'],
})
export class CoachListComponent implements OnInit {
  availableCoaches: CoachInformation[] = []
  userUuid: string

  constructor(
    private coachListService: CoachListService,
    private keycloakAngular: KeycloakService,
    private toast: MessageService
  ) {
    this.userUuid = this.keycloakAngular.getKeycloakInstance().subject
    this.getAvailableCoaches(this.userUuid)

    // AppService.blockui.emit(true)
  }

  ngOnInit(): void {}

  private getAvailableCoaches(userId): void {
    this.coachListService.getCoachesAvailableForUser(userId).subscribe((api) => {
      this.availableCoaches.push(...api)
    })
  }

  createWorkoutPlan(coachId: string): void {
    this.coachListService.createWorkoutPlan(this.userUuid, coachId).subscribe((wk) => {
      const coach = this.changeAvailableCoachStatus(wk.coachId, true)
      this.toast.add(
        ToastMsg.build(
          ToastSeverity.SUCCESS,
          'Success !',
          `Workout plan created with coach ${coach.name} !`
        )
      )
    })
  }

  cancelCreationWorkoutPlan(coachId: string): void {
    this.coachListService.cancelPendingWorkoutPlan(this.userUuid, coachId).subscribe((wk) => {
      const coach = this.changeAvailableCoachStatus(wk.coachId, false)
      this.toast.add(
        ToastMsg.build(
          ToastSeverity.WARN,
          'Hire canceled !',
          `Pending workout plan with coach ${coach.name} was canceled !`
        )
      )
    })
  }

  private changeAvailableCoachStatus(coachId, status): CoachInformation {
    let coachChanged: CoachInformation
    this.availableCoaches.forEach((coach) => {
      if (coach.id === coachId) {
        coachChanged = coach
        coach.isWorkoutPending = status
      }
    })
    return coachChanged
  }
}
