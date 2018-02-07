/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SniperTestModule } from '../../../test.module';
import { SnFileDialogComponent } from '../../../../../../main/webapp/app/entities/sn-file/sn-file-dialog.component';
import { SnFileService } from '../../../../../../main/webapp/app/entities/sn-file/sn-file.service';
import { SnFile } from '../../../../../../main/webapp/app/entities/sn-file/sn-file.model';
import { SnFileBlobService } from '../../../../../../main/webapp/app/entities/sn-file-blob';
import { PatientService } from '../../../../../../main/webapp/app/entities/patient';
import { TaskService } from '../../../../../../main/webapp/app/entities/task';
import { UserInfoService } from '../../../../../../main/webapp/app/entities/user-info';

describe('Component Tests', () => {

    describe('SnFile Management Dialog Component', () => {
        let comp: SnFileDialogComponent;
        let fixture: ComponentFixture<SnFileDialogComponent>;
        let service: SnFileService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [SnFileDialogComponent],
                providers: [
                    SnFileBlobService,
                    PatientService,
                    TaskService,
                    UserInfoService,
                    SnFileService
                ]
            })
            .overrideTemplate(SnFileDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SnFileDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SnFileService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SnFile(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.snFile = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'snFileListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SnFile();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.snFile = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'snFileListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
