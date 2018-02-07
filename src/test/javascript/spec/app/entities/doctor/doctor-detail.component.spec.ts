/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SniperTestModule } from '../../../test.module';
import { DoctorDetailComponent } from '../../../../../../main/webapp/app/entities/doctor/doctor-detail.component';
import { DoctorService } from '../../../../../../main/webapp/app/entities/doctor/doctor.service';
import { Doctor } from '../../../../../../main/webapp/app/entities/doctor/doctor.model';

describe('Component Tests', () => {

    describe('Doctor Management Detail Component', () => {
        let comp: DoctorDetailComponent;
        let fixture: ComponentFixture<DoctorDetailComponent>;
        let service: DoctorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [DoctorDetailComponent],
                providers: [
                    DoctorService
                ]
            })
            .overrideTemplate(DoctorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DoctorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DoctorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Doctor(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.doctor).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
