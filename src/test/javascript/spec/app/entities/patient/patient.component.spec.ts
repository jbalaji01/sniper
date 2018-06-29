/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SniperTestModule } from '../../../test.module';
import { PatientComponent } from '../../../../../../main/webapp/app/entities/patient/patient.component';
import { PatientService } from '../../../../../../main/webapp/app/entities/patient/patient.service';
import { Patient } from '../../../../../../main/webapp/app/entities/patient/patient.model';

describe('Component Tests', () => {

    describe('Patient Management Component', () => {
        let comp: PatientComponent;
        let fixture: ComponentFixture<PatientComponent>;
        let service: PatientService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [PatientComponent],
                providers: [
                    PatientService
                ]
            })
            .overrideTemplate(PatientComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PatientComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PatientService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Patient(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.patients[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
