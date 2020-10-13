import { ToastSeverity } from '../enums/ToastSeverity.enum'

export class ToastMsg {
  readonly severity: ToastSeverity
  readonly summary: string
  readonly detail: string

  private constructor(severity: ToastSeverity, summary: string, detail: string) {
    this.severity = severity
    this.summary = summary
    this.detail = detail
  }

  static build(severity: ToastSeverity, summary: string, detail: string): ToastMsg {
    return new ToastMsg(severity, summary, detail)
  }
}
