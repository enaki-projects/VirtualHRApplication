import {Component, EventEmitter, forwardRef, Input, Output} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';

/**
 * Lightweight replacement for ngx-chips' <tag-input> (abandoned, no Angular 22 build).
 * Preserves the behaviour the CV skills editor relied on:
 *  - two-way [(ngModel)] binding to an array of model objects,
 *  - (onAdd)/(onRemove) events emitting the added/removed model object,
 *  - identifyBy/displayBy to map model objects to a key/label,
 *  - an optional autocomplete dropdown fed by [autocompleteItems].
 *
 * Added tags echo the shape ngx-chips produced: an object carrying the display
 * field plus (when not matched from autocomplete) a null identify field.
 */
@Component({
  selector: 'app-skill-tag-input',
  templateUrl: './skill-tag-input.component.html',
  styleUrls: ['./skill-tag-input.component.css'],
  standalone: false,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SkillTagInputComponent),
      multi: true
    }
  ]
})
export class SkillTagInputComponent implements ControlValueAccessor {
  @Input() placeholder = 'Add...';
  @Input() displayBy = 'name';
  @Input() identifyBy = 'id';
  @Input() theme = '';
  @Input() autocompleteItems: any[] = [];

  @Output() onAdd = new EventEmitter<any>();
  @Output() onRemove = new EventEmitter<any>();

  tags: any[] = [];
  inputValue = '';
  showDropdown = false;

  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  // --- ControlValueAccessor ---
  writeValue(value: any[]): void {
    this.tags = Array.isArray(value) ? value : [];
  }
  registerOnChange(fn: (value: any) => void): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  get filteredItems(): any[] {
    const q = this.inputValue.trim().toLowerCase();
    return (this.autocompleteItems || []).filter((item) => {
      const label = String(this.labelOf(item) ?? '').toLowerCase();
      const already = this.tags.some((t) => this.sameTag(t, item));
      return !already && (q === '' || label.includes(q));
    });
  }

  labelOf(item: any): string {
    if (item == null) {
      return '';
    }
    return typeof item === 'object' ? item[this.displayBy] : item;
  }

  private sameTag(a: any, b: any): boolean {
    if (a == null || b == null) {
      return false;
    }
    const ka = typeof a === 'object' ? a[this.identifyBy] : a;
    const kb = typeof b === 'object' ? b[this.identifyBy] : b;
    if (ka != null && kb != null) {
      return ka === kb;
    }
    return this.labelOf(a) === this.labelOf(b);
  }

  onFocus(): void {
    this.showDropdown = true;
  }

  onBlur(): void {
    this.onTouched();
    // Delay so a mousedown on a dropdown item is handled before hiding.
    setTimeout(() => (this.showDropdown = false), 150);
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Enter' || event.key === ',') {
      event.preventDefault();
      this.addFromInput();
    } else if (event.key === 'Backspace' && this.inputValue === '' && this.tags.length) {
      this.removeTag(this.tags[this.tags.length - 1]);
    }
  }

  addFromInput(): void {
    const value = this.inputValue.trim();
    if (!value) {
      return;
    }
    // Prefer an exact autocomplete match so the identify field is preserved.
    const match = (this.autocompleteItems || []).find(
      (item) => String(this.labelOf(item)).toLowerCase() === value.toLowerCase()
    );
    const tag = match ?? {[this.displayBy]: value, [this.identifyBy]: null};
    this.commitAdd(tag);
  }

  selectItem(item: any): void {
    this.commitAdd(item);
  }

  private commitAdd(tag: any): void {
    if (this.tags.some((t) => this.sameTag(t, tag))) {
      this.inputValue = '';
      return;
    }
    this.tags = [...this.tags, tag];
    this.inputValue = '';
    this.showDropdown = false;
    this.onChange(this.tags);
    this.onAdd.emit(tag);
  }

  removeTag(tag: any): void {
    this.tags = this.tags.filter((t) => t !== tag);
    this.onChange(this.tags);
    this.onRemove.emit(tag);
  }
}
