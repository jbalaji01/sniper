/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SniperTestModule } from '../../../test.module';
import { TaskGroupDetailComponent } from '../../../../../../main/webapp/app/entities/task-group/task-group-detail.component';
import { TaskGroupService } from '../../../../../../main/webapp/app/entities/task-group/task-group.service';
import { TaskGroup } from '../../../../../../main/webapp/app/entities/task-group/task-group.model';

describe('Component Tests', () => {

    describe('TaskGroup Management Detail Component', () => {
        let comp: TaskGroupDetailComponent;
        let fixture: ComponentFixture<TaskGroupDetailComponent>;
        let service: TaskGroupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [TaskGroupDetailComponent],
                providers: [
                    TaskGroupService
                ]
            })
            .overrideTemplate(TaskGroupDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskGroupDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskGroupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TaskGroup(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.taskGroup).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
