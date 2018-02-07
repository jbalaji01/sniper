import { BaseEntity } from './../../shared';

export const enum TaskStatus {
    'CREATED',
    'ASSIGNED',
    'DOWNLOADED',
    'UPLOADED',
    'IN_PROGRESS',
    'COMPLETED',
    'ON_HOLD',
    'QUERY',
    'MERGED',
    'DELETED',
    'TRANSFERRED'
}

export class TaskHistory implements BaseEntity {
    constructor(
        public id?: number,
        public taskStatus?: TaskStatus,
        public punchTime?: any,
        public notes?: string,
        public task?: BaseEntity,
        public userInfo?: BaseEntity,
    ) {
    }
}
