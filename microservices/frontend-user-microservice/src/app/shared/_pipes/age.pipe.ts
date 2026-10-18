import {Pipe, PipeTransform} from '@angular/core';
import moment from 'moment';

@Pipe({
  standalone: false,
  name: 'age'
})
export class AgePipe implements PipeTransform {

  transform(value: Date): string {
    const today = moment();
    const birthdate = moment(value);
    const years = today.diff(birthdate, 'years');
    // html += today.subtract(years, 'years').diff(birthdate, 'months') + ' mo';

    return years.toString();
  }

}
