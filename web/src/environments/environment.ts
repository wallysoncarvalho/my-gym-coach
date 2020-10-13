// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import { KeycloakConfig } from 'keycloak-angular'

let keycloakConfig: KeycloakConfig = {
  url: 'http://localhost:8197/auth',
  realm: 'mygymcoach',
  clientId: 'angular-front',
}

export const environment = {
  production: false,
  keycloak: keycloakConfig,
}

export const apiUrl = {
  athlete: 'http://localhost:8090/api/athletes',
  coach: 'http://localhost:8090/api/coaches',
  workout: 'http://localhost:8090/api/workouts',
  workoutDay: 'http://localhost:8090/api/workout-day',
  exercise: 'http://localhost:8090/api/exercise',
}

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
