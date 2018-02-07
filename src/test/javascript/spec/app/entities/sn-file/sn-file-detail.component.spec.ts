/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SniperTestModule } from '../../../test.module';
import { SnFileDetailComponent } from '../../../../../../main/webapp/app/entities/sn-file/sn-file-detail.component';
import { SnFileService } from '../../../../../../main/webapp/app/entities/sn-file/sn-file.service';
import { SnFile } from '../../../../../../main/webapp/app/entities/sn-file/sn-file.model';

describe('Component Tests', () => {

    describe('SnFile Management Detail Component', () => {
        let comp: SnFileDetailComponent;
        let fixture: ComponentFixture<SnFileDetailComponent>;
        let service: SnFileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [SnFileDetailComponent],
                providers: [
                    SnFileService
                ]
            })
            .overrideTemplate(SnFileDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SnFileDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SnFileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new SnFile(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.snFile).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
