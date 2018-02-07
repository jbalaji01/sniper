/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SniperTestModule } from '../../../test.module';
import { DoctorComponent } from '../../../../../../main/webapp/app/entities/doctor/doctor.component';
import { DoctorService } from '../../../../../../main/webapp/app/entities/doctor/doctor.service';
import { Doctor } from '../../../../../../main/webapp/app/entities/doctor/doctor.model';

describe('Component Tests', () => {

    describe('Doctor Management Component', () => {
        let comp: DoctorComponent;
        let fixture: ComponentFixture<DoctorComponent>;
        let service: DoctorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [DoctorComponent],
                providers: [
                    DoctorService
                ]
            })
            .overrideTemplate(DoctorComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DoctorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DoctorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Doctor(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.doctors[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
