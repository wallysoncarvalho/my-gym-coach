import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { CanAuthenticationGuard } from './auth/CanAuthenticationGuard'

import {
  HomeComponent,
  CoachListComponent,
  WorkoutplanListComponent,
  WorkoutPlanComponent,
  WorkoutDaysComponent,
  ExercisesPageComponent,
} from './components'

const routes: Routes = [
  // {
  //   path: 'main',
  //   loadChildren: () => import('./pages/main/main.module').then((m) => m.MainModule),
  //   canActivate: [CanAuthenticationGuard],
  //   data: { roles: ['TESTE'] },
  // },
  // {
  //   path: '**',
  //   redirectTo: 'main',
  // },
  {
    path: '',
    component: WorkoutPlanComponent,
    // component: HomeComponent,
    // canActivate: [CanAuthenticationGuard],
    children: [
      {
        path: 'workoutdays',
        component: WorkoutDaysComponent,
      },
    ],
  },
  {
    path: 'available-coaches',
    component: CoachListComponent,
    canActivate: [CanAuthenticationGuard],
  },
  {
    path: 'workout-plans',
    component: WorkoutplanListComponent,
    canActivate: [CanAuthenticationGuard],
  },
  {
    path: 'workout-plan/:id',
    component: WorkoutPlanComponent,
    canActivate: [CanAuthenticationGuard],
  },
  {
    path: 'exercises',
    component: ExercisesPageComponent,
  },
  {
    path: '**',
    redirectTo: '/',
  },
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [CanAuthenticationGuard],
})
export class AppRoutingModule {}
