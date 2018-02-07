import { BaseEntity } from './../../shared';

export class Hospital implements BaseEntity {
    constructor(
        public id?: number,
        public hospitalName?: string,
        public city?: string,
        public templateCount?: number,
        public notes?: string,
        public tasks?: BaseEntity[],
        public doctors?: BaseEntity[],
    ) {
    }
}
