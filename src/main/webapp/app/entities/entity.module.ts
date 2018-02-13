import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SniperUserInfoModule } from './user-info/user-info.module';
import { SniperHospitalModule } from './hospital/hospital.module';
import { SniperDoctorModule } from './doctor/doctor.module';
import { SniperCompanyModule } from './company/company.module';
import { SniperPatientModule } from './patient/patient.module';
import { SniperSnFileModule } from './sn-file/sn-file.module';
import { SniperSnFileBlobModule } from './sn-file-blob/sn-file-blob.module';
import { SniperTaskGroupModule } from './task-group/task-group.module';
import { SniperTaskModule } from './task/task.module';
import { SniperTaskHistoryModule } from './task-history/task-history.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

import {ExtTaskModule} from './ext-task/ext-task.module';

@NgModule({
    imports: [
        SniperUserInfoModule,
        SniperHospitalModule,
        SniperDoctorModule,
        SniperCompanyModule,
        SniperPatientModule,
        SniperSnFileModule,
        SniperSnFileBlobModule,
        SniperTaskGroupModule,
        SniperTaskModule,
        SniperTaskHistoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
        ExtTaskModule,
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SniperEntityModule {}
