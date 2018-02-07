/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SniperTestModule } from '../../../test.module';
import { TaskGroupComponent } from '../../../../../../main/webapp/app/entities/task-group/task-group.component';
import { TaskGroupService } from '../../../../../../main/webapp/app/entities/task-group/task-group.service';
import { TaskGroup } from '../../../../../../main/webapp/app/entities/task-group/task-group.model';

describe('Component Tests', () => {

    describe('TaskGroup Management Component', () => {
        let comp: TaskGroupComponent;
        let fixture: ComponentFixture<TaskGroupComponent>;
        let service: TaskGroupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [TaskGroupComponent],
                providers: [
                    TaskGroupService
                ]
            })
            .overrideTemplate(TaskGroupComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskGroupComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskGroupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TaskGroup(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.taskGroups[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
