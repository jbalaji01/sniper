/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SniperTestModule } from '../../../test.module';
import { PatientDetailComponent } from '../../../../../../main/webapp/app/entities/patient/patient-detail.component';
import { PatientService } from '../../../../../../main/webapp/app/entities/patient/patient.service';
import { Patient } from '../../../../../../main/webapp/app/entities/patient/patient.model';

describe('Component Tests', () => {

    describe('Patient Management Detail Component', () => {
        let comp: PatientDetailComponent;
        let fixture: ComponentFixture<PatientDetailComponent>;
        let service: PatientService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [PatientDetailComponent],
                providers: [
                    PatientService
                ]
            })
            .overrideTemplate(PatientDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PatientDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PatientService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Patient(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.patient).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
