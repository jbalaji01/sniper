import { BaseEntity } from './../../shared';

export class Patient implements BaseEntity {
    constructor(
        public id?: number,
        public patientName?: string,
        public city?: string,
        public notes?: string,
        public snFiles?: BaseEntity[],
    ) {
    }
}
