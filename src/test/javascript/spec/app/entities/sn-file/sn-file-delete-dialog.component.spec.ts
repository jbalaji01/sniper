/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SniperTestModule } from '../../../test.module';
import { SnFileDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/sn-file/sn-file-delete-dialog.component';
import { SnFileService } from '../../../../../../main/webapp/app/entities/sn-file/sn-file.service';

describe('Component Tests', () => {

    describe('SnFile Management Delete Component', () => {
        let comp: SnFileDeleteDialogComponent;
        let fixture: ComponentFixture<SnFileDeleteDialogComponent>;
        let service: SnFileService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [SnFileDeleteDialogComponent],
                providers: [
                    SnFileService
                ]
            })
            .overrideTemplate(SnFileDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SnFileDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SnFileService);
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
