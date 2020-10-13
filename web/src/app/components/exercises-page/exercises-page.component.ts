import { Component, OnInit } from '@angular/core'
import { Muscle } from 'src/app/shared/interfaces/Muscle'
import { FormGroup, FormBuilder, Validators } from '@angular/forms'
import { ExercisesPageService } from './exercises-page.service'

@Component({
  selector: 'app-exercises-page',
  templateUrl: './exercises-page.component.html',
  styleUrls: ['./exercises-page.component.scss'],
})
export class ExercisesPageComponent implements OnInit {
  exerciseForm: FormGroup
  muscles: Muscle[] = []
  selectedMuscles: Muscle[] = []
  exercises: Exercise[]
  exerciseTableColumns: any[]
  first = 0
  tableRows = 5

  constructor(private formBuilder: FormBuilder, private exerciseService: ExercisesPageService) {
    this.exerciseService.getPaginatedMuscles().subscribe((muscles) => {
      this.muscles = muscles
    })

    this.exerciseService
      .getPaginatedExercises()
      .subscribe((exercises) => (this.exercises = exercises))

    this.exerciseTableColumns = [
      { field: 'name', header: 'Name' },
      { field: 'description', header: 'Description' },
    ]

    this.exerciseForm = this.formBuilder.group({
      muscles: [[], Validators.required],
      name: ['', Validators.required],
      description: [''],
    })
  }

  ngOnInit(): void {}

  addExercise(value) {
    console.log(value)
  }
}
