import { BaseEntity } from './../../shared';

export enum ChosenFactor {
    'NONE',
    'TIME_FRAME',
    'WS_LINE_COUNT',
    'WOS_LINE_COUNT'
}

export class SnFile implements BaseEntity {
    constructor(
        public id?: number,
        public filePath?: string,
        public fileName?: string,
        public fileExt?: string,
        public origin?: string,
        public isInput?: boolean,
        public isAudio?: boolean,
        public uploadedTime?: any,
        public actualTimeFrame?: number,
        public adjustedTimeFrame?: number,
        public finalTimeFrame?: number,
        public wsActualLineCount?: number,
        public wsAdjustedLineCount?: number,
        public wsFinalLineCount?: number,
        public wosActualLineCount?: number,
        public wosAdjustedLineCount?: number,
        public wosFinalLineCount?: number,
        public chosenFactor?: ChosenFactor,
        public peckOrder?: number,
        public snFileBlob?: BaseEntity,
        public patients?: BaseEntity[],
        public tasks?: BaseEntity[],
        public uploader?: BaseEntity,
    ) {
        this.isInput = false;
        this.isAudio = false;
    }
}
