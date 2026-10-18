import {Component, Input, Self} from '@angular/core';
import {ControlValueAccessor, NgControl} from '@angular/forms';

@Component({
  standalone: false,
  selector: 'app-select-input',
  templateUrl: './select-input.component.html',
  styleUrls: ['./select-input.component.css']
})
export class SelectInputComponent implements ControlValueAccessor {
  @Input() label: string;
  @Input() textToDisplay = '';
  @Input() defaultValue = '';
  @Input() type = 'text';
  @Input() options: string[] = [];

  constructor(@Self() public ngControl: NgControl) {
    this.ngControl.valueAccessor = this;
  }

  writeValue(obj: any): void {
  }

  registerOnChange(fn: any): void {
  }

  registerOnTouched(fn: any): void {
  }

  startWithVowelRegEx(str: string): boolean {
    if (str !== undefined && str.length >= 1) {
      return /[aeiou]/.test(str[0].toLowerCase());
    }
    return false;
  }

}
