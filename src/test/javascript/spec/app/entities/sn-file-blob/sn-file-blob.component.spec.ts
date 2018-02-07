/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SniperTestModule } from '../../../test.module';
import { SnFileBlobComponent } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob.component';
import { SnFileBlobService } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob.service';
import { SnFileBlob } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob.model';

describe('Component Tests', () => {

    describe('SnFileBlob Management Component', () => {
        let comp: SnFileBlobComponent;
        let fixture: ComponentFixture<SnFileBlobComponent>;
        let service: SnFileBlobService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [SnFileBlobComponent],
                providers: [
                    SnFileBlobService
                ]
            })
            .overrideTemplate(SnFileBlobComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SnFileBlobComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SnFileBlobService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SnFileBlob(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.snFileBlobs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
