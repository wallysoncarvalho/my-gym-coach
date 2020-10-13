import { Component, OnInit } from '@angular/core'
import { RouterLink, Router, ActivatedRoute, ActivatedRouteSnapshot } from '@angular/router'
import { AppService } from './app.service'
import { KeycloakService } from 'keycloak-angular'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'mgc-web'
  showUserOptions = false
  hideSideNav = false
  showProgressBar = false
  username = 'Anonym'
  currentComponentName: string
  blockuiMsg: BlockuiMessage = { blockui: false }

  constructor(private keycloakService: KeycloakService) {

    AppService.blockui.subscribe((blockuiMsg) => {
      this.blockuiMsg = blockuiMsg
    })

    AppService.showProgressBar.subscribe((val) => {
      this.showProgressBar = val
    })
  }

  async ngOnInit() {
    if (await this.keycloakService.isLoggedIn()) {
      this.username = await this.keycloakService.getKeycloakInstance().profile.firstName
    }
  }

  async logout() {
    await this.keycloakService.logout()
  }

  closeBlockUi() {
    const temp = { blockui: false }
    AppService.blockui.emit(temp)
  }

  toggleSideNavView() {
    this.hideSideNav = !this.hideSideNav
  }
}
