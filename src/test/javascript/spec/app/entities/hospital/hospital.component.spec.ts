/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SniperTestModule } from '../../../test.module';
import { HospitalComponent } from '../../../../../../main/webapp/app/entities/hospital/hospital.component';
import { HospitalService } from '../../../../../../main/webapp/app/entities/hospital/hospital.service';
import { Hospital } from '../../../../../../main/webapp/app/entities/hospital/hospital.model';

describe('Component Tests', () => {

    describe('Hospital Management Component', () => {
        let comp: HospitalComponent;
        let fixture: ComponentFixture<HospitalComponent>;
        let service: HospitalService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [HospitalComponent],
                providers: [
                    HospitalService
                ]
            })
            .overrideTemplate(HospitalComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HospitalComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HospitalService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Hospital(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.hospitals[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
