/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SniperTestModule } from '../../../test.module';
import { UserInfoDetailComponent } from '../../../../../../main/webapp/app/entities/user-info/user-info-detail.component';
import { UserInfoService } from '../../../../../../main/webapp/app/entities/user-info/user-info.service';
import { UserInfo } from '../../../../../../main/webapp/app/entities/user-info/user-info.model';

describe('Component Tests', () => {

    describe('UserInfo Management Detail Component', () => {
        let comp: UserInfoDetailComponent;
        let fixture: ComponentFixture<UserInfoDetailComponent>;
        let service: UserInfoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [UserInfoDetailComponent],
                providers: [
                    UserInfoService
                ]
            })
            .overrideTemplate(UserInfoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserInfoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserInfoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new UserInfo(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.userInfo).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
