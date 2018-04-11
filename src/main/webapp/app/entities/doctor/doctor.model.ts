import { BaseEntity } from './../../shared';

export const enum ChosenFactor {
    'NONE',
    'TIME_FRAME',
    'WS_LINE_COUNT',
    'WOS_LINE_COUNT'
}

export class Doctor implements BaseEntity {
    constructor(
        public id?: number,
        public doctorName?: string,
        public city?: string,
        public templateCount?: number,
        public chosenFactor?: ChosenFactor,
        public notes?: string,
        public tasks?: BaseEntity[],
        public hospitals?: BaseEntity[],
    ) {
    }
}
