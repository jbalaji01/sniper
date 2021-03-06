import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TaskGroup } from './task-group.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TaskGroup>;

@Injectable()
export class TaskGroupService {

    private resourceUrl =  SERVER_API_URL + 'api/task-groups';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(taskGroup: TaskGroup): Observable<EntityResponseType> {
        const copy = this.convert(taskGroup);
        return this.http.post<TaskGroup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(taskGroup: TaskGroup): Observable<EntityResponseType> {
        const copy = this.convert(taskGroup);
        return this.http.put<TaskGroup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TaskGroup>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TaskGroup[]>> {
        const options = createRequestOption(req);
        return this.http.get<TaskGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TaskGroup[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    public convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TaskGroup = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    public convertArrayResponse(res: HttpResponse<TaskGroup[]>): HttpResponse<TaskGroup[]> {
        const jsonResponse: TaskGroup[] = res.body;
        const body: TaskGroup[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TaskGroup.
     */
    public convertItemFromServer(taskGroup: TaskGroup): TaskGroup {
        const copy: TaskGroup = Object.assign({}, taskGroup);
        copy.createdTime = this.dateUtils
            .convertDateTimeFromServer(taskGroup.createdTime);
        return copy;
    }

    /**
     * Convert a TaskGroup to a JSON which can be sent to the server.
     */
    public convert(taskGroup: TaskGroup): TaskGroup {
        const copy: TaskGroup = Object.assign({}, taskGroup);

        copy.createdTime = this.dateUtils.toDate(taskGroup.createdTime);
        return copy;
    }
}
