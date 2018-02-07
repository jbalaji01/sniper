import { BaseEntity, User } from './../../shared';

export class UserInfo implements BaseEntity {
    constructor(
        public id?: number,
        public empCode?: string,
        public phone?: string,
        public dateOfBirth?: any,
        public dateOfJoin?: any,
        public bankInfo?: string,
        public pan?: string,
        public addr?: string,
        public city?: string,
        public lastLogin?: any,
        public user?: User,
        public snFiles?: BaseEntity[],
        public ownerTasks?: BaseEntity[],
        public transcriptTasks?: BaseEntity[],
        public editorTasks?: BaseEntity[],
        public managerTasks?: BaseEntity[],
        public taskHistories?: BaseEntity[],
        public company?: BaseEntity,
    ) {
    }
}
