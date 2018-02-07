/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SniperTestModule } from '../../../test.module';
import { SnFileBlobDialogComponent } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob-dialog.component';
import { SnFileBlobService } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob.service';
import { SnFileBlob } from '../../../../../../main/webapp/app/entities/sn-file-blob/sn-file-blob.model';
import { SnFileService } from '../../../../../../main/webapp/app/entities/sn-file';

describe('Component Tests', () => {

    describe('SnFileBlob Management Dialog Component', () => {
        let comp: SnFileBlobDialogComponent;
        let fixture: ComponentFixture<SnFileBlobDialogComponent>;
        let service: SnFileBlobService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [SnFileBlobDialogComponent],
                providers: [
                    SnFileService,
                    SnFileBlobService
                ]
            })
            .overrideTemplate(SnFileBlobDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SnFileBlobDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SnFileBlobService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SnFileBlob(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.snFileBlob = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'snFileBlobListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SnFileBlob();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.snFileBlob = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'snFileBlobListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
