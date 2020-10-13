interface SideNavMenu {
  readonly name: string
  active?: boolean
  readonly content: MenuItems[]
}

interface MenuItems {
  readonly name: string
  readonly location: string
}
