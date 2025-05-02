import {animate, group, query, style, transition, trigger} from '@angular/animations';

export const slider =
  trigger('routeAnimations', [
    transition(':increment', slideTo('right') ),
    transition(':decrement', slideTo('left') ),
  ]);

function slideTo(direction): any {
  const optional = { optional: true };
  return [
    query(':enter, :leave', [
      style({
        position: 'absolute',
        [direction]: 0,
        width: '100%'
      })
    ], optional),
    query(':enter', [
      style({ [direction]: '-100%'})
    ]),
    group([
      query(':leave', [
        animate('600ms ease', style({ [direction]: '100%'}))
      ], optional),
      query(':enter', [
        animate('600ms ease', style({ [direction]: '0%'}))
      ])
    ]),
  ];
}
