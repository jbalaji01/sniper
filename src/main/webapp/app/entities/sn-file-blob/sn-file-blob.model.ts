import { BaseEntity } from './../../shared';

export class SnFileBlob implements BaseEntity {
    constructor(
        public id?: number,
        public fileContentContentType?: string,
        public fileContent?: any,
        public snFile?: BaseEntity,
    ) {
    }
}
