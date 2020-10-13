import { Pipe, PipeTransform } from '@angular/core'
import { DatePipe } from '@angular/common'

@Pipe({
  name: 'dateFormat',
})
export class DateFormatPipe extends DatePipe implements PipeTransform {
  transform(value: any, args?: any): any {
    // const pattern = /(\d{4})\-(\d{2})\-(\d{2})/
    // return value.replace(pattern, '$3/$2/$1')
    return super.transform(value, 'dd/MM/y')
  }
}
