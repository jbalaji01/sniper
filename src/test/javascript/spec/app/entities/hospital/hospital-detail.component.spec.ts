/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SniperTestModule } from '../../../test.module';
import { HospitalDetailComponent } from '../../../../../../main/webapp/app/entities/hospital/hospital-detail.component';
import { HospitalService } from '../../../../../../main/webapp/app/entities/hospital/hospital.service';
import { Hospital } from '../../../../../../main/webapp/app/entities/hospital/hospital.model';

describe('Component Tests', () => {

    describe('Hospital Management Detail Component', () => {
        let comp: HospitalDetailComponent;
        let fixture: ComponentFixture<HospitalDetailComponent>;
        let service: HospitalService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [HospitalDetailComponent],
                providers: [
                    HospitalService
                ]
            })
            .overrideTemplate(HospitalDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HospitalDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HospitalService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Hospital(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.hospital).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
