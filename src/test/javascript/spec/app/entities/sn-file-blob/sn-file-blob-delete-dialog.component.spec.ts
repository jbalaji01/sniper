/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SniperTestModule } from '../../../test.module';
import { SnFileBlobDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob-delete-dialog.component';
import { SnFileBlobService } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob.service';

describe('Component Tests', () => {

    describe('SnFileBlob Management Delete Component', () => {
        let comp: SnFileBlobDeleteDialogComponent;
        let fixture: ComponentFixture<SnFileBlobDeleteDialogComponent>;
        let service: SnFileBlobService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [SnFileBlobDeleteDialogComponent],
                providers: [
                    SnFileBlobService
                ]
            })
            .overrideTemplate(SnFileBlobDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SnFileBlobDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SnFileBlobService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
