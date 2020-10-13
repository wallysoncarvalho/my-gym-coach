import { Component, OnInit } from '@angular/core'
import { Router, ActivatedRoute, ParamMap } from '@angular/router'
import { switchMap } from 'rxjs/operators'
import { MenuItem } from 'primeng/api'

@Component({
  selector: 'app-workout-plan',
  templateUrl: './workout-plan.component.html',
  styleUrls: ['./workout-plan.component.scss'],
})
export class WorkoutPlanComponent implements OnInit {
  workoutPlanId: string
  workoutMenu: MenuItem[]
  activeItem: MenuItem

  constructor(private route: ActivatedRoute) {
    this.workoutPlanId = this.route.snapshot.paramMap.get('id') || 'fail, bro !'
  }

  ngOnInit(): void {
    this.workoutMenu = [
      { label: 'Workout Plan', icon: 'pi pi-id-card', routerLink: 'workoutdays' },
      { label: 'Body Measures', icon: 'pi pi-user', routerLink: 'body-measures' },
      { label: 'Personal Quiz', icon: 'pi pi-question-circle', routerLink: 'quiz' },
    ]

    this.activeItem = this.workoutMenu[0]
  }
}
