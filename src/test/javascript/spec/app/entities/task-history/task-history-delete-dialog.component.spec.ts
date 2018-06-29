/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SniperTestModule } from '../../../test.module';
import { TaskHistoryDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/task-history/task-history-delete-dialog.component';
import { TaskHistoryService } from '../../../../../../main/webapp/app/entities/task-history/task-history.service';

describe('Component Tests', () => {

    describe('TaskHistory Management Delete Component', () => {
        let comp: TaskHistoryDeleteDialogComponent;
        let fixture: ComponentFixture<TaskHistoryDeleteDialogComponent>;
        let service: TaskHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [TaskHistoryDeleteDialogComponent],
                providers: [
                    TaskHistoryService
                ]
            })
            .overrideTemplate(TaskHistoryDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskHistoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskHistoryService);
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
