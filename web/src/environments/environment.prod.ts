import { KeycloakConfig } from 'keycloak-angular'

let keycloakConfig: KeycloakConfig = {
  url: 'http://localhost:8197/auth',
  realm: 'mygymcoach',
  clientId: 'angular-front',
}

export const environment = {
  production: true,
  keycloak: keycloakConfig,
}

export const apiUrl = {
  athlete: 'http://localhost:8090/api/athletes',
  coach: 'http://localhost:8090/api/coaches',
  workout: 'http://localhost:8090/api/workouts',
  workoutDay: 'http://localhost:8090/api/workout-day',
  exercise: 'http://localhost:8090/api/exercise',
}
