/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SniperTestModule } from '../../../test.module';
import { SnFileComponent } from '../../../../../../main/webapp/app/entities/sn-file/sn-file.component';
import { SnFileService } from '../../../../../../main/webapp/app/entities/sn-file/sn-file.service';
import { SnFile } from '../../../../../../main/webapp/app/entities/sn-file/sn-file.model';

describe('Component Tests', () => {

    describe('SnFile Management Component', () => {
        let comp: SnFileComponent;
        let fixture: ComponentFixture<SnFileComponent>;
        let service: SnFileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [SnFileComponent],
                providers: [
                    SnFileService
                ]
            })
            .overrideTemplate(SnFileComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SnFileComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SnFileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SnFile(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.snFiles[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
