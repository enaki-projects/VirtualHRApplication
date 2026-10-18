import {Component} from '@angular/core';
import {slider} from './shared/_animations/slider';
import {RouterOutlet} from '@angular/router';

@Component({
  standalone: false,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    slider
  ],
})
export class AppComponent {
  title = 'frontend-user-microservice';

  prepareRoute(outlet: RouterOutlet): any {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData.animation;
  }
}
