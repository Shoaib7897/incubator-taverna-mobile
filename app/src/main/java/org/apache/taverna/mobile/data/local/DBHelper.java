package org.apache.taverna.mobile.data.local;


import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.data.model.Workflow_Table;
import org.apache.taverna.mobile.data.model.Workflows;

import android.support.annotation.Nullable;

import rx.Observable;
import rx.Subscriber;


public class DBHelper {


    public DBHelper() {

    }

    @Nullable
    public Observable<Workflows> syncWorkflows(final Workflows workflows) {
        return Observable.create(new Observable.OnSubscribe<Workflows>() {
            @Override
            public void call(Subscriber<? super Workflows> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                for (Workflow workflow : workflows.getWorkflowList()) {
                    if (!workflow.exists()) {
                        workflow.save();

                    } else {

                        updateWorkflow(workflow).save();
                    }

                }
                subscriber.onNext(workflows);
                subscriber.onCompleted();
            }
        });
    }

    private Workflow updateWorkflow(Workflow workflow) {

        Workflow workflow1 = SQLite.select()
                .from(Workflow.class)
                .where(Workflow_Table.id.eq(workflow.getId()))
                .querySingle();

        if (workflow1 != null) {
            if (workflow.getDescription() != null) {

                workflow1.setDescription(workflow.getDescription());
            }
            if (workflow.getUpdatedAt() != null ) {

                workflow1.setUpdatedAt(workflow.getUpdatedAt());
            }
            if (workflow.getSvgUri() != null ) {

                workflow1.setSvgUri(workflow.getSvgUri());
            }
            if (workflow.getLicenseType() != null ) {

                workflow1.setLicenseType(workflow.getLicenseType());
            }
            if (workflow.getContentUri() != null ) {

                workflow1.setContentUri(workflow.getContentUri());
            }
            if (workflow.getContentType() != null ) {

                workflow1.setContentUri(workflow.getContentType());
            }

            workflow1.setTitle(workflow.getTitle());
            workflow1.setType(workflow.getType());
            workflow1.setUploader(workflow.getUploader());
            workflow1.setPreviewUri(workflow.getPreviewUri());
            workflow1.setCreatedAt(workflow.getCreatedAt());
            workflow1.setResource(workflow.getResource());
            workflow1.setUri(workflow.getUri());
            workflow1.setId(workflow.getId());
            workflow1.setVersion(workflow.getVersion());

        }
        return workflow1;
    }


    public Observable<Workflow> syncWorkflow(final Workflow workflow) {
        return Observable.create(new Observable.OnSubscribe<Workflow>() {
            @Override
            public void call(Subscriber<? super Workflow> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (!workflow.exists()) {
                    workflow.save();

                } else {

                    updateWorkflow(workflow).save();
                }
                subscriber.onNext(workflow);
                subscriber.onCompleted();
            }
        });
    }


}
