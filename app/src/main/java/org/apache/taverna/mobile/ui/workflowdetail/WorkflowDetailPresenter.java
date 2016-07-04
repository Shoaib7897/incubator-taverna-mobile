package org.apache.taverna.mobile.ui.workflowdetail;

import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.model.License;
import org.apache.taverna.mobile.data.model.User;
import org.apache.taverna.mobile.data.model.Workflow;
import org.apache.taverna.mobile.ui.base.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WorkflowDetailPresenter extends BasePresenter<WorkflowDetailMvpView> {

    public final String LOG_TAG = getClass().getSimpleName();
    private DataManager mDataManager;
    private Subscription mSubscriptions;


    public WorkflowDetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(WorkflowDetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void loadWorkflowDetail(String id) {
        getMvpView().showProgressbar(true);
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
        mSubscriptions = mDataManager.getDetailWorkflow(id, getDetailQueryOptions())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Workflow>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onNext(Workflow workflow) {
                        getMvpView().showWorkflowDetail(workflow);
                        loadUserDetail(workflow.getUploader().getId());
                    }
                });

    }

    private void loadUserDetail(String id) {

        getMvpView().showProgressbar(true);
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
        mSubscriptions = mDataManager.getUserDetail(id, getUserQueryOptions())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onNext(User user) {
                        getMvpView().setImage(user);
                    }
                });
    }

    public void loadLicenseDetail(String id) {
        getMvpView().showLicenseProgress(true);

        if (mSubscriptions != null) mSubscriptions.unsubscribe();
        mSubscriptions = mDataManager.getLicenseDetail(id, getLicenceQueryOptions())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<License>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showLicenseProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showLicenseProgress(false);
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onNext(License license) {
                        getMvpView().showLicense(license);
                    }
                });
    }

    public void setFavourite(String id) {

        if (mSubscriptions != null) mSubscriptions.unsubscribe();

        mSubscriptions = mDataManager.setFavoriteWorkflow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onNext(Boolean b) {
                        if (b) {
                            getMvpView().setFavouriteIcon();
                            getMvpView().showErrorSnackBar("Add to Favourite ");
                        } else {
                            getMvpView().showErrorSnackBar("Something went wrong please try after" +
                                    "sometime");
                        }

                    }
                });
    }

    public void getFavourite(String id) {

        if (mSubscriptions != null) mSubscriptions.unsubscribe();

        mSubscriptions = mDataManager.getFavoriteWorkflow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar("Something went wrong please try after " +
                                "sometime");
                    }

                    @Override
                    public void onNext(Boolean b) {
                        getMvpView().getFavouriteIcon(b);
                    }
                });
    }

    private Map<String, String> getDetailQueryOptions() {

        Map<String, String> option = new HashMap<>();
        option.put("elements", "id,title,type,uploader,preview,created-at,svg,updated-at," +
                "description,license-type,tags");
        return option;
    }

    private Map<String, String> getUserQueryOptions() {

        Map<String, String> option = new HashMap<>();
        option.put("elements", "avatar");
        return option;
    }

    private Map<String, String> getLicenceQueryOptions() {

        Map<String, String> option = new HashMap<>();
        option.put("elements", "title,description,url,created-at");
        return option;
    }


}
