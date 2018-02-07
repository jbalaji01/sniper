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

export class Task implements BaseEntity {
    constructor(
        public id?: number,
        public taskTitle?: string,
        public taskStatus?: TaskStatus,
        public createdTime?: any,
        public hasPMSignedOff?: boolean,
        public isActive?: boolean,
        public peckOrder?: number,
        public notes?: string,
        public taskHistories?: BaseEntity[],
        public company?: BaseEntity,
        public taskGroup?: BaseEntity,
        public owner?: BaseEntity,
        public transcript?: BaseEntity,
        public editor?: BaseEntity,
        public manager?: BaseEntity,
        public doctor?: BaseEntity,
        public hospital?: BaseEntity,
        public snFiles?: BaseEntity[],
    ) {
        this.hasPMSignedOff = false;
        this.isActive = false;
    }
}
