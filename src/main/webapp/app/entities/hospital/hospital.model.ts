import { BaseEntity } from './../../shared';

export const enum ChosenFactor {
    'NONE',
    'TIME_FRAME',
    'WS_LINE_COUNT',
    'WOS_LINE_COUNT'
}

export class Hospital implements BaseEntity {
    constructor(
        public id?: number,
        public hospitalName?: string,
        public city?: string,
        public templateCount?: number,
        public chosenFactor?: ChosenFactor,
        public notes?: string,
        public tasks?: BaseEntity[],
        public doctors?: BaseEntity[],
    ) {
    }
}
