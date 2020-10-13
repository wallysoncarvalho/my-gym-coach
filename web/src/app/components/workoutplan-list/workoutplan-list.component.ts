import { Component, OnInit } from '@angular/core'
import { WorkoutplanListService } from './workoutplan-list.service'
import { ConfirmationService } from 'primeng/api'
import { KeycloakService } from 'keycloak-angular'
import { forkJoin } from 'rxjs'

@Component({
  selector: 'app-workoutplan-list',
  templateUrl: './workoutplan-list.component.html',
  styleUrls: ['./workoutplan-list.component.scss'],
})
export class WorkoutplanListComponent implements OnInit {
  workoutPlanList: WorkoutPlan[] = []

  constructor(
    private wkPlanListService: WorkoutplanListService,
    private confirmationService: ConfirmationService,
    private keycloakService: KeycloakService
  ) {
    const userUuid = this.keycloakService.getKeycloakInstance().subject

    this.wkPlanListService.getAllUsersWkPlans(userUuid).subscribe((wkPlans) => {
      this.workoutPlanList.push(...wkPlans)
    })
  }

  ngOnInit(): void {}

  acceptOrDenyHire(wkplanId) {
    const wkPlan = this.workoutPlanList.find((wk) => wk.id === wkplanId)
    this.confirmationService.confirm({
      header: 'Accept Hire',
      message: `The user ${wkPlan.athleteName} wants to start a workout
                plan with your mentoring. Do you accept it ?`,
      accept: () => {
        this.wkPlanListService.changeWkPlanStatus(wkplanId, 'ACTIVE').subscribe((wk) => {
          this.workoutPlanList.forEach((w) => {
            if (w.id == wk.id) {
              w.wkStatus = wk.wkStatus
            }
          })
        })
      },
      reject: () => {
        this.wkPlanListService.changeWkPlanStatus(wkplanId, 'DECLINED').subscribe()
      },
    })
  }
}
