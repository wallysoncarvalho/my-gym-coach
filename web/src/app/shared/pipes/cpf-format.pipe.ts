import { Pipe, PipeTransform } from '@angular/core'

@Pipe({
  name: 'cpfFormat',
})
export class CpfFormatPipe implements PipeTransform {
  transform(value: string, ...args: any[]): any {
    return value.length === 11
      ? value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')
      : value
  }
}
