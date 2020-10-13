interface UserInformation {
  readonly id?: string
  readonly firstName?: string
  readonly lastName?: string
  readonly username?: string
  readonly email?: string
  readonly dateOfRegister?: Date
  readonly attributes?: UserAttributes
}

interface UserAttributes {
  readonly cpf: string
  readonly cell: string
  readonly address: string
}
