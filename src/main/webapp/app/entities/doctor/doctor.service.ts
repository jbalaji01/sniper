import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Doctor } from './doctor.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Doctor>;

@Injectable()
export class DoctorService {

    private resourceUrl =  SERVER_API_URL + 'api/doctors';

    constructor(private http: HttpClient) { }

    create(doctor: Doctor): Observable<EntityResponseType> {
        const copy = this.convert(doctor);
        return this.http.post<Doctor>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(doctor: Doctor): Observable<EntityResponseType> {
        const copy = this.convert(doctor);
        return this.http.put<Doctor>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Doctor>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Doctor[]>> {
        const options = createRequestOption(req);
        return this.http.get<Doctor[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Doctor[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Doctor = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Doctor[]>): HttpResponse<Doctor[]> {
        const jsonResponse: Doctor[] = res.body;
        const body: Doctor[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Doctor.
     */
    private convertItemFromServer(doctor: Doctor): Doctor {
        const copy: Doctor = Object.assign({}, doctor);
        return copy;
    }

    /**
     * Convert a Doctor to a JSON which can be sent to the server.
     */
    private convert(doctor: Doctor): Doctor {
        const copy: Doctor = Object.assign({}, doctor);
        return copy;
    }
}
