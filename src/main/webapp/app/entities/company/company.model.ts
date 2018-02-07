import { BaseEntity } from './../../shared';

export class Company implements BaseEntity {
    constructor(
        public id?: number,
        public companyName?: string,
        public city?: string,
        public notes?: string,
        public userInfos?: BaseEntity[],
        public tasks?: BaseEntity[],
    ) {
    }
}
