import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { SnFile } from './sn-file.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<SnFile>;

@Injectable()
export class SnFileService {

    private resourceUrl =  SERVER_API_URL + 'api/sn-files';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(snFile: SnFile): Observable<EntityResponseType> {
        const copy = this.convert(snFile);
        return this.http.post<SnFile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(snFile: SnFile): Observable<EntityResponseType> {
        const copy = this.convert(snFile);
        return this.http.put<SnFile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<SnFile>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<SnFile[]>> {
        const options = createRequestOption(req);
        return this.http.get<SnFile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SnFile[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: SnFile = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<SnFile[]>): HttpResponse<SnFile[]> {
        const jsonResponse: SnFile[] = res.body;
        const body: SnFile[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to SnFile.
     */
    private convertItemFromServer(snFile: SnFile): SnFile {
        const copy: SnFile = Object.assign({}, snFile);
        copy.uploadedTime = this.dateUtils
            .convertDateTimeFromServer(snFile.uploadedTime);
        return copy;
    }

    /**
     * Convert a SnFile to a JSON which can be sent to the server.
     */
    private convert(snFile: SnFile): SnFile {
        const copy: SnFile = Object.assign({}, snFile);

        copy.uploadedTime = this.dateUtils.toDate(snFile.uploadedTime);
        return copy;
    }
}
