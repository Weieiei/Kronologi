'use strict';


customElements.define('compodoc-menu', class extends HTMLElement {
    constructor() {
        super();
        this.isNormalMode = this.getAttribute('mode') === 'normal';
    }

    connectedCallback() {
        this.render(this.isNormalMode);
    }

    render(isNormalMode) {
        let tp = lithtml.html(`
        <nav>
            <ul class="list">
                <li class="title">
                    <a href="index.html" data-type="index-link">appointment-scheduler documentation</a>
                </li>

                <li class="divider"></li>
                ${ isNormalMode ? `<div id="book-search-input" role="search"><input type="text" placeholder="Type to search"></div>` : '' }
                <li class="chapter">
                    <a data-type="chapter-link" href="index.html"><span class="icon ion-ios-home"></span>Getting started</a>
                    <ul class="links">
                        <li class="link">
                            <a href="overview.html" data-type="chapter-link">
                                <span class="icon ion-ios-keypad"></span>Overview
                            </a>
                        </li>
                        <li class="link">
                            <a href="index.html" data-type="chapter-link">
                                <span class="icon ion-ios-paper"></span>README
                            </a>
                        </li>
                        <li class="link">
                            <a href="dependencies.html" data-type="chapter-link">
                                <span class="icon ion-ios-list"></span>Dependencies
                            </a>
                        </li>
                    </ul>
                </li>
                    <li class="chapter modules">
                        <a data-type="chapter-link" href="modules.html">
                            <div class="menu-toggler linked" data-toggle="collapse" ${ isNormalMode ?
                                'data-target="#modules-links"' : 'data-target="#xs-modules-links"' }>
                                <span class="icon ion-ios-archive"></span>
                                <span class="link-name">Modules</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                        </a>
                        <ul class="links collapse" ${ isNormalMode ? 'id="modules-links"' : 'id="xs-modules-links"' }>
                            <li class="link">
                                <a href="modules/AppModule.html" data-type="entity-link">AppModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' : 'data-target="#xs-components-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' :
                                            'id="xs-components-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' }>
                                            <li class="link">
                                                <a href="components/AccountSettingsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AccountSettingsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AddRecurringShiftFormComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AddRecurringShiftFormComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AddShiftFormComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AddShiftFormComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AdminAppointmentsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AdminAppointmentsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AdminEmployeesComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AdminEmployeesComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AdminServicesComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AdminServicesComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AdminUsersComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AdminUsersComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AppComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppointmentCardComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AppointmentCardComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppointmentsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AppointmentsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppointmentsSectionComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AppointmentsSectionComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AssignServicesDialogComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AssignServicesDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BookComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">BookComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BusinessRegisterComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">BusinessRegisterComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BusinessViewComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">BusinessViewComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CancelDialogComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CancelDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CardsUiComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CardsUiComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ChangeClientToEmployeeDialogComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ChangeClientToEmployeeDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CreateServiceComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CreateServiceComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CustomStepperComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CustomStepperComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DashboardComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">DashboardComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DetailsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">DetailsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/EmployeeAppointmentsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">EmployeeAppointmentsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ErrorDialogComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ErrorDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FindBusinessDialogComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">FindBusinessDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/GoogleMapsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">GoogleMapsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/HomeComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">HomeComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/LoginComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">LoginComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/NavbarComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">NavbarComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/NotesAndReserveComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">NotesAndReserveComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/PickDayComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">PickDayComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ReasonDialogComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ReasonDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/RegisterComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">RegisterComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ReminderSettingsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ReminderSettingsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ReviewComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ReviewComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SearchInputComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SearchInputComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ServiceSelectionGridListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ServiceSelectionGridListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SettingsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SettingsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ShiftComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ShiftComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ShiftPickerComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ShiftPickerComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SyncCalendarsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SyncCalendarsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TimePickerDialogComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">TimePickerDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/VerifiedComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">VerifiedComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                                <li class="chapter inner">
                                    <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                        'data-target="#directives-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' : 'data-target="#xs-directives-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' }>
                                        <span class="icon ion-md-code-working"></span>
                                        <span>Directives</span>
                                        <span class="icon ion-ios-arrow-down"></span>
                                    </div>
                                    <ul class="links collapse" ${ isNormalMode ? 'id="directives-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' :
                                        'id="xs-directives-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' }>
                                        <li class="link">
                                            <a href="directives/AutofocusDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">AutofocusDirective</a>
                                        </li>
                                        <li class="link">
                                            <a href="directives/ButtonIconDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">ButtonIconDirective</a>
                                        </li>
                                        <li class="link">
                                            <a href="directives/CardSizeDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">CardSizeDirective</a>
                                        </li>
                                        <li class="link">
                                            <a href="directives/FabBottomRightDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">FabBottomRightDirective</a>
                                        </li>
                                    </ul>
                                </li>
                                <li class="chapter inner">
                                    <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                        'data-target="#injectables-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' : 'data-target="#xs-injectables-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' }>
                                        <span class="icon ion-md-arrow-round-down"></span>
                                        <span>Injectables</span>
                                        <span class="icon ion-ios-arrow-down"></span>
                                    </div>
                                    <ul class="links collapse" ${ isNormalMode ? 'id="injectables-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' :
                                        'id="xs-injectables-links-module-AppModule-e05cff37310f46947b0f4399ed4bf075"' }>
                                        <li class="link">
                                            <a href="injectables/AuthService.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules" }>AuthService</a>
                                        </li>
                                    </ul>
                                </li>
                            </li>
                            <li class="link">
                                <a href="modules/AppRoutingModule.html" data-type="entity-link">AppRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/MaterialModule.html" data-type="entity-link">MaterialModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ThemeModule.html" data-type="entity-link">ThemeModule</a>
                            </li>
                </ul>
                </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#classes-links"' :
                            'data-target="#xs-classes-links"' }>
                            <span class="icon ion-ios-paper"></span>
                            <span>Classes</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse" ${ isNormalMode ? 'id="classes-links"' : 'id="xs-classes-links"' }>
                            <li class="link">
                                <a href="classes/Appointment.html" data-type="entity-link">Appointment</a>
                            </li>
                            <li class="link">
                                <a href="classes/AppointmentDetailed.html" data-type="entity-link">AppointmentDetailed</a>
                            </li>
                            <li class="link">
                                <a href="classes/EmployeeShift.html" data-type="entity-link">EmployeeShift</a>
                            </li>
                            <li class="link">
                                <a href="classes/EmployeeShiftTimes.html" data-type="entity-link">EmployeeShiftTimes</a>
                            </li>
                            <li class="link">
                                <a href="classes/PasswordMismatchStateMatcher.html" data-type="entity-link">PasswordMismatchStateMatcher</a>
                            </li>
                            <li class="link">
                                <a href="classes/Service.html" data-type="entity-link">Service</a>
                            </li>
                            <li class="link">
                                <a href="classes/User.html" data-type="entity-link">User</a>
                            </li>
                            <li class="link">
                                <a href="classes/UserToDisplay.html" data-type="entity-link">UserToDisplay</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#injectables-links"' :
                                'data-target="#xs-injectables-links"' }>
                                <span class="icon ion-md-arrow-round-down"></span>
                                <span>Injectables</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                            <ul class="links collapse" ${ isNormalMode ? 'id="injectables-links"' : 'id="xs-injectables-links"' }>
                                <li class="link">
                                    <a href="injectables/AdminService.html" data-type="entity-link">AdminService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/AppointmentService.html" data-type="entity-link">AppointmentService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/AuthService.html" data-type="entity-link">AuthService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/BusinessService.html" data-type="entity-link">BusinessService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ClientGuard.html" data-type="entity-link">ClientGuard</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/EmployeeService.html" data-type="entity-link">EmployeeService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/GeocodingApiService.html" data-type="entity-link">GeocodingApiService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/GoogleAnalyticsService.html" data-type="entity-link">GoogleAnalyticsService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/HelperService.html" data-type="entity-link">HelperService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ImageResizeService.html" data-type="entity-link">ImageResizeService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ReviewService.html" data-type="entity-link">ReviewService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ServiceService.html" data-type="entity-link">ServiceService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/SnackBar.html" data-type="entity-link">SnackBar</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ThemeService.html" data-type="entity-link">ThemeService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/UserService.html" data-type="entity-link">UserService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/VerificationService.html" data-type="entity-link">VerificationService</a>
                                </li>
                            </ul>
                        </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#interceptors-links"' :
                            'data-target="#xs-interceptors-links"' }>
                            <span class="icon ion-ios-swap"></span>
                            <span>Interceptors</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse" ${ isNormalMode ? 'id="interceptors-links"' : 'id="xs-interceptors-links"' }>
                            <li class="link">
                                <a href="interceptors/UrlInterceptor.html" data-type="entity-link">UrlInterceptor</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#guards-links"' :
                            'data-target="#xs-guards-links"' }>
                            <span class="icon ion-ios-lock"></span>
                            <span>Guards</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse" ${ isNormalMode ? 'id="guards-links"' : 'id="xs-guards-links"' }>
                            <li class="link">
                                <a href="guards/AdminGuard.html" data-type="entity-link">AdminGuard</a>
                            </li>
                            <li class="link">
                                <a href="guards/AnonymousGuard.html" data-type="entity-link">AnonymousGuard</a>
                            </li>
                            <li class="link">
                                <a href="guards/AuthGuard.html" data-type="entity-link">AuthGuard</a>
                            </li>
                            <li class="link">
                                <a href="guards/EmployeeGuard.html" data-type="entity-link">EmployeeGuard</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#interfaces-links"' :
                            'data-target="#xs-interfaces-links"' }>
                            <span class="icon ion-md-information-circle-outline"></span>
                            <span>Interfaces</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse" ${ isNormalMode ? ' id="interfaces-links"' : 'id="xs-interfaces-links"' }>
                            <li class="link">
                                <a href="interfaces/AdminEmployeeDTO.html" data-type="entity-link">AdminEmployeeDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/AdminEmployeeShiftDTO.html" data-type="entity-link">AdminEmployeeShiftDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Appointment.html" data-type="entity-link">Appointment</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/AppointmentDetail.html" data-type="entity-link">AppointmentDetail</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/AppointmentServiceDTO.html" data-type="entity-link">AppointmentServiceDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/BookAppointmentDTO.html" data-type="entity-link">BookAppointmentDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Business.html" data-type="entity-link">Business</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/BusinessDTO.html" data-type="entity-link">BusinessDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/BusinessHours.html" data-type="entity-link">BusinessHours</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/BusinessHoursDTO.html" data-type="entity-link">BusinessHoursDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/BusinessRegisterDTO.html" data-type="entity-link">BusinessRegisterDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/BusinessUserRegisterDTO.html" data-type="entity-link">BusinessUserRegisterDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/CancelAppointmentDTO.html" data-type="entity-link">CancelAppointmentDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/DateDTO.html" data-type="entity-link">DateDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/DialogData.html" data-type="entity-link">DialogData</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/DialogData-1.html" data-type="entity-link">DialogData</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/DialogData-2.html" data-type="entity-link">DialogData</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Domain.html" data-type="entity-link">Domain</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/EmployeeAppointmentDTO.html" data-type="entity-link">EmployeeAppointmentDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/EmployeeDTO.html" data-type="entity-link">EmployeeDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/EmployeeFreeTime.html" data-type="entity-link">EmployeeFreeTime</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/EmployeeTimes.html" data-type="entity-link">EmployeeTimes</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/NewShiftDTO.html" data-type="entity-link">NewShiftDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/PhoneNumberDTO.html" data-type="entity-link">PhoneNumberDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/ReviewDTO.html" data-type="entity-link">ReviewDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Service.html" data-type="entity-link">Service</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/ServiceCreateDto.html" data-type="entity-link">ServiceCreateDto</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/ServiceDTO.html" data-type="entity-link">ServiceDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/SettingsDTO.html" data-type="entity-link">SettingsDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Shift.html" data-type="entity-link">Shift</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/ShiftDTO.html" data-type="entity-link">ShiftDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Slot.html" data-type="entity-link">Slot</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Time.html" data-type="entity-link">Time</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/TimeDTO.html" data-type="entity-link">TimeDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/UpdateEmailDTO.html" data-type="entity-link">UpdateEmailDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/UpdatePasswordDTO.html" data-type="entity-link">UpdatePasswordDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/UpdateSettingsDTO.html" data-type="entity-link">UpdateSettingsDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/User.html" data-type="entity-link">User</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/UserAppointmentDTO.html" data-type="entity-link">UserAppointmentDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/UserLoginDTO.html" data-type="entity-link">UserLoginDTO</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/UserRegisterDTO.html" data-type="entity-link">UserRegisterDTO</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#miscellaneous-links"'
                            : 'data-target="#xs-miscellaneous-links"' }>
                            <span class="icon ion-ios-cube"></span>
                            <span>Miscellaneous</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse" ${ isNormalMode ? 'id="miscellaneous-links"' : 'id="xs-miscellaneous-links"' }>
                            <li class="link">
                                <a href="miscellaneous/enumerations.html" data-type="entity-link">Enums</a>
                            </li>
                            <li class="link">
                                <a href="miscellaneous/variables.html" data-type="entity-link">Variables</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <a data-type="chapter-link" href="routes.html"><span class="icon ion-ios-git-branch"></span>Routes</a>
                        </li>
                    <li class="chapter">
                        <a data-type="chapter-link" href="coverage.html"><span class="icon ion-ios-stats"></span>Documentation coverage</a>
                    </li>
                    <li class="divider"></li>
                    <li class="copyright">
                        Documentation generated using <a href="https://compodoc.app/" target="_blank">
                            <img data-src="images/compodoc-vectorise.png" class="img-responsive" data-type="compodoc-logo">
                        </a>
                    </li>
            </ul>
        </nav>
        `);
        this.innerHTML = tp.strings;
    }
});