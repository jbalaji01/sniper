import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Patient } from './patient.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Patient>;

@Injectable()
export class PatientService {

    private resourceUrl =  SERVER_API_URL + 'api/patients';

    constructor(private http: HttpClient) { }

    create(patient: Patient): Observable<EntityResponseType> {
        const copy = this.convert(patient);
        return this.http.post<Patient>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(patient: Patient): Observable<EntityResponseType> {
        const copy = this.convert(patient);
        return this.http.put<Patient>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Patient>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Patient[]>> {
        const options = createRequestOption(req);
        return this.http.get<Patient[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Patient[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Patient = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Patient[]>): HttpResponse<Patient[]> {
        const jsonResponse: Patient[] = res.body;
        const body: Patient[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Patient.
     */
    private convertItemFromServer(patient: Patient): Patient {
        const copy: Patient = Object.assign({}, patient);
        return copy;
    }

    /**
     * Convert a Patient to a JSON which can be sent to the server.
     */
    private convert(patient: Patient): Patient {
        const copy: Patient = Object.assign({}, patient);
        return copy;
    }
}
