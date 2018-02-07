import { BaseEntity } from './../../shared';

export class Doctor implements BaseEntity {
    constructor(
        public id?: number,
        public doctorName?: string,
        public city?: string,
        public templateCount?: number,
        public notes?: string,
        public tasks?: BaseEntity[],
        public hospitals?: BaseEntity[],
    ) {
    }
}
