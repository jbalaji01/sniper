import { BaseEntity } from './../../shared';

export class TaskGroup implements BaseEntity {
    constructor(
        public id?: number,
        public groupName?: string,
        public createdTime?: any,
        public tasks?: BaseEntity[],
    ) {
    }
}
