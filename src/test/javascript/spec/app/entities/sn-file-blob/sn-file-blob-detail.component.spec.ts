/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SniperTestModule } from '../../../test.module';
import { SnFileBlobDetailComponent } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob-detail.component';
import { SnFileBlobService } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob.service';
import { SnFileBlob } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob.model';

describe('Component Tests', () => {

    describe('SnFileBlob Management Detail Component', () => {
        let comp: SnFileBlobDetailComponent;
        let fixture: ComponentFixture<SnFileBlobDetailComponent>;
        let service: SnFileBlobService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [SnFileBlobDetailComponent],
                providers: [
                    SnFileBlobService
                ]
            })
            .overrideTemplate(SnFileBlobDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SnFileBlobDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SnFileBlobService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new SnFileBlob(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.snFileBlob).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
