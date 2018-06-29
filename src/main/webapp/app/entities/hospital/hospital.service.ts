import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Hospital } from './hospital.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Hospital>;

@Injectable()
export class HospitalService {

    private resourceUrl =  SERVER_API_URL + 'api/hospitals';

    constructor(private http: HttpClient) { }

    create(hospital: Hospital): Observable<EntityResponseType> {
        const copy = this.convert(hospital);
        return this.http.post<Hospital>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(hospital: Hospital): Observable<EntityResponseType> {
        const copy = this.convert(hospital);
        return this.http.put<Hospital>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Hospital>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Hospital[]>> {
        const options = createRequestOption(req);
        return this.http.get<Hospital[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Hospital[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Hospital = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Hospital[]>): HttpResponse<Hospital[]> {
        const jsonResponse: Hospital[] = res.body;
        const body: Hospital[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Hospital.
     */
    private convertItemFromServer(hospital: Hospital): Hospital {
        const copy: Hospital = Object.assign({}, hospital);
        return copy;
    }

    /**
     * Convert a Hospital to a JSON which can be sent to the server.
     */
    private convert(hospital: Hospital): Hospital {
        const copy: Hospital = Object.assign({}, hospital);
        return copy;
    }
}
