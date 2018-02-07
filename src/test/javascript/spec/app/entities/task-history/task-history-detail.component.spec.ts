/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SniperTestModule } from '../../../test.module';
import { TaskHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/task-history/task-history-detail.component';
import { TaskHistoryService } from '../../../../../../main/webapp/app/entities/task-history/task-history.service';
import { TaskHistory } from '../../../../../../main/webapp/app/entities/task-history/task-history.model';

describe('Component Tests', () => {

    describe('TaskHistory Management Detail Component', () => {
        let comp: TaskHistoryDetailComponent;
        let fixture: ComponentFixture<TaskHistoryDetailComponent>;
        let service: TaskHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [TaskHistoryDetailComponent],
                providers: [
                    TaskHistoryService
                ]
            })
            .overrideTemplate(TaskHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TaskHistory(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.taskHistory).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
