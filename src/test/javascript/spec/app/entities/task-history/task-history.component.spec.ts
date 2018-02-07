/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SniperTestModule } from '../../../test.module';
import { TaskHistoryComponent } from '../../../../../../main/webapp/app/entities/task-history/task-history.component';
import { TaskHistoryService } from '../../../../../../main/webapp/app/entities/task-history/task-history.service';
import { TaskHistory } from '../../../../../../main/webapp/app/entities/task-history/task-history.model';

describe('Component Tests', () => {

    describe('TaskHistory Management Component', () => {
        let comp: TaskHistoryComponent;
        let fixture: ComponentFixture<TaskHistoryComponent>;
        let service: TaskHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [TaskHistoryComponent],
                providers: [
                    TaskHistoryService
                ]
            })
            .overrideTemplate(TaskHistoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskHistoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TaskHistory(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.taskHistories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
