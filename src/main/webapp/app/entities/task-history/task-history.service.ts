import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TaskHistory } from './task-history.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TaskHistory>;

@Injectable()
export class TaskHistoryService {

    private resourceUrl =  SERVER_API_URL + 'api/task-histories';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(taskHistory: TaskHistory): Observable<EntityResponseType> {
        const copy = this.convert(taskHistory);
        return this.http.post<TaskHistory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(taskHistory: TaskHistory): Observable<EntityResponseType> {
        const copy = this.convert(taskHistory);
        return this.http.put<TaskHistory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TaskHistory>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TaskHistory[]>> {
        const options = createRequestOption(req);
        return this.http.get<TaskHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TaskHistory[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TaskHistory = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TaskHistory[]>): HttpResponse<TaskHistory[]> {
        const jsonResponse: TaskHistory[] = res.body;
        const body: TaskHistory[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TaskHistory.
     */
    private convertItemFromServer(taskHistory: TaskHistory): TaskHistory {
        const copy: TaskHistory = Object.assign({}, taskHistory);
        copy.punchTime = this.dateUtils
            .convertDateTimeFromServer(taskHistory.punchTime);
        return copy;
    }

    /**
     * Convert a TaskHistory to a JSON which can be sent to the server.
     */
    private convert(taskHistory: TaskHistory): TaskHistory {
        const copy: TaskHistory = Object.assign({}, taskHistory);

        copy.punchTime = this.dateUtils.toDate(taskHistory.punchTime);
        return copy;
    }
}
