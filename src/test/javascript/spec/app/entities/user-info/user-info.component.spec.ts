/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SniperTestModule } from '../../../test.module';
import { UserInfoComponent } from '../../../../../../main/webapp/app/entities/user-info/user-info.component';
import { UserInfoService } from '../../../../../../main/webapp/app/entities/user-info/user-info.service';
import { UserInfo } from '../../../../../../main/webapp/app/entities/user-info/user-info.model';

describe('Component Tests', () => {

    describe('UserInfo Management Component', () => {
        let comp: UserInfoComponent;
        let fixture: ComponentFixture<UserInfoComponent>;
        let service: UserInfoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SniperTestModule],
                declarations: [UserInfoComponent],
                providers: [
                    UserInfoService
                ]
            })
            .overrideTemplate(UserInfoComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserInfoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserInfoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new UserInfo(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.userInfos[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
