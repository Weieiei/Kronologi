import { Injectable } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { Router, NavigationEnd } from '@angular/router';

//declare the function that is found in the  scripts/google folder
declare let ga: Function;

@Injectable({
    providedIn: 'root'
})

/** */
export class GoogleAnalyticsService {


  private subscription: Subscription;


  constructor(private router: Router) {

  }

  /**
   * Track an event with your custom data in google analytics
   * -
   * @param  cat object that will  be seen on the analytics website
   * @param interaction form of interaction (i.e: login , submit, etc...)
   * @param category Useful for categorizing events (e.g. 'Fall Campaign')
   * @param value   value associated with the event (e.g. 42)
   * @memberof GoogleAnalyticsEventService
   */
  public trackValues(cat: string, interaction: string, category: string = null, value: number = null) {
    try {
      //ga function declared in the scripts 
      ga('send', 'event', { eventCategory: cat, eventLabel: interaction,
        eventAction: category, eventValue: value
      } );
    } catch (error) {
      console.log(`error: ${error}`);
    }
  }


  public subscribe() {
    if (!this.subscription) {
      // subscribe to any router navigation and once it ends, write out the google script notices
      this.subscription = this.router.events.subscribe( e => {
        if (e instanceof NavigationEnd) {
          // this will find & use the ga function pulled via the google scripts
          try {
            ga('set', 'page', e.urlAfterRedirects);
            ga('send', 'pageview');
          } catch {
            console.log('tracking not found');
          }
        }
      });
    }
  }

  public unsubscribe() {
      // need to clear the subscription
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}