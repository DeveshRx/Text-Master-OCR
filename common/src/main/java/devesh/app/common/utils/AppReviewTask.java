package devesh.app.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.Task;

import devesh.app.common.BuildConfig;
import devesh.app.common.R;


public class AppReviewTask {
    ReviewManager manager;
    Context mContext;
    Activity mActivity;
    String TAG = "AppReviewTask";
    SharedPreferences AppSharedPref;

    public AppReviewTask(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;

        if (BuildConfig.DEBUG) {
            manager = new FakeReviewManager(context);
        } else {
            manager = ReviewManagerFactory.create(context);
        }
        AppSharedPref = activity.getPreferences(Context.MODE_PRIVATE);

    }

    public void requestAppReview() {
        Task<ReviewInfo> request = manager.requestReviewFlow();

        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(mActivity, reviewInfo);

                flow.addOnCompleteListener(taskf -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    Log.d(TAG, "AppReviewTask: " + task.getResult().toString());
                    setIsAppReviewed(true);
                });

            } else {
                // There was some problem, log or handle the error code.
                //@ReviewErrorCode int reviewErrorCode = ((TaskException) task.getException()).getErrorCode();

            }
        });
    }

    public boolean isAppReviewed() {
        return AppSharedPref.getBoolean(mContext.getString(R.string.Pref_isAppReviewed), false);
    }

    public void setIsAppReviewed(boolean isReviewed) {
        SharedPreferences.Editor editor = AppSharedPref.edit();
        editor.putBoolean(mContext.getString(R.string.Pref_isAppReviewed), isReviewed);
        editor.apply();
    }

}
