import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { UserInfo } from './user-info.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<UserInfo>;

@Injectable()
export class UserInfoService {

    private resourceUrl =  SERVER_API_URL + 'api/user-infos';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(userInfo: UserInfo): Observable<EntityResponseType> {
        const copy = this.convert(userInfo);
        return this.http.post<UserInfo>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(userInfo: UserInfo): Observable<EntityResponseType> {
        const copy = this.convert(userInfo);
        return this.http.put<UserInfo>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<UserInfo>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<UserInfo[]>> {
        const options = createRequestOption(req);
        return this.http.get<UserInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<UserInfo[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: UserInfo = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<UserInfo[]>): HttpResponse<UserInfo[]> {
        const jsonResponse: UserInfo[] = res.body;
        const body: UserInfo[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to UserInfo.
     */
    private convertItemFromServer(userInfo: UserInfo): UserInfo {
        const copy: UserInfo = Object.assign({}, userInfo);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateFromServer(userInfo.dateOfBirth);
        copy.dateOfJoin = this.dateUtils
            .convertLocalDateFromServer(userInfo.dateOfJoin);
        copy.lastLogin = this.dateUtils
            .convertDateTimeFromServer(userInfo.lastLogin);
        return copy;
    }

    /**
     * Convert a UserInfo to a JSON which can be sent to the server.
     */
    private convert(userInfo: UserInfo): UserInfo {
        const copy: UserInfo = Object.assign({}, userInfo);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateToServer(userInfo.dateOfBirth);
        copy.dateOfJoin = this.dateUtils
            .convertLocalDateToServer(userInfo.dateOfJoin);

        copy.lastLogin = this.dateUtils.toDate(userInfo.lastLogin);
        return copy;
    }
}
