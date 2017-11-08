package com.pugfish1992.hammock.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.google.common.base.Preconditions.checkNotNull;
import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Work;
import com.pugfish1992.hammock.ui.adapter.CommentAdapter;
import com.pugfish1992.hammock.ui.adapter.WorkAdapter;
import com.pugfish1992.hammock.ui.decoration.SpacerItemDecoration;
import com.pugfish1992.hammock.ui.utils.BottomSheetCallbackHelper;
import com.pugfish1992.hammock.ui.utils.ScrollingWatcher;
import com.pugfish1992.hammock.util.ResUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class WorkDetailsActivity extends AppCompatActivity
        implements WorkAdapter.ItemCardClickListener {

    public static final String KEY_TARGET_WORK_ID = "WorkDetailsActivity:targetWorkId";

    private Work mTargetWork;
    private WorkAdapter mSubWorkAdapter;
    private CommentAdapter mCommentAdapter;

    // Sub-Works Viewer Bottom Sheet
    private BottomSheetBehavior mSubWorksViewerSheetBehavior;
    private View mSubWorksViewerScrim;
    private SubWorksViewerBsHeaderBinder mSubWorksViewerHeaderBinder;

    // Comments Viewer Bottom Sheet
    private BottomSheetBehavior mCommentsViewerSheetBehavior;
    private View mCommentsViewerScrim;
    private CommentsViewerBsHeaderBinder mCommentsViewerHeaderBinder;

    public static Bundle makeExtras(@NonNull Work targetWork) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_TARGET_WORK_ID, checkNotNull(targetWork).getId());
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(KEY_TARGET_WORK_ID)) {
            long id = extras.getLong(KEY_TARGET_WORK_ID);
            mTargetWork = new Work(id);

        } else {
            throw new IllegalStateException(
                    "pass the id of a target work to this activity");
        }

        mSubWorkAdapter = new WorkAdapter(mTargetWork.getSubWorks(), this, this);
        mCommentAdapter = new CommentAdapter(mTargetWork.getComments());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupSubWorksViewerBottomSheet();
        setupCommentsViewerBottomSheet();
        invalidateBottomSheetTitleMessage();
    }

    /**
     * BOTTOM SHEETS
     * ---------------- */

    private void setupSubWorksViewerBottomSheet() {
        ResUtils resUtils = new ResUtils(this);

        View root = findViewById(R.id.fl_works_viewer_bs_root);
        ViewCompat.setElevation(root, resUtils.getPx(R.dimen.sub_works_viewer_bs_elevation));

        mSubWorksViewerScrim = findViewById(R.id.view_works_viewer_bs_scrim);

        mSubWorksViewerHeaderBinder = new SubWorksViewerBsHeaderBinder(
                findViewById(R.id.include_works_viewer_bs_header));
        mSubWorksViewerHeaderBinder.setTitleMessage("");

        RecyclerView subWorkList = findViewById(R.id.recycler_works_viewer_bs_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        subWorkList.setLayoutManager(layoutManager);
        subWorkList.setAdapter(mSubWorkAdapter);

        SpacerItemDecoration spacerItemDecoration = new SpacerItemDecoration();
        spacerItemDecoration.setTopSpace(resUtils.getPx(R.dimen.sub_works_viewer_bs_list_top_offset));
        spacerItemDecoration.setLeftSpace(resUtils.getPx(R.dimen.sub_works_viewer_bs_list_side_offset));
        spacerItemDecoration.setRightSpace(resUtils.getPx(R.dimen.sub_works_viewer_bs_list_side_offset));
        spacerItemDecoration.setBottomSpace(resUtils.getPx(R.dimen.sub_works_viewer_bs_list_bottom_offset));
        spacerItemDecoration.setSpaceBetweenItems(resUtils.getPx(R.dimen.sub_works_viewer_bs_list_items_offset));
        subWorkList.addItemDecoration(spacerItemDecoration);

        // Fade out the header along with scrolling
        ScrollingWatcher scrollingWatcher = new ScrollingWatcher(subWorkList);
        scrollingWatcher.setCallback(new ScrollingWatcher.Callback() {
            @Override
            public void onScrolled(ScrollingWatcher scrollingWatcher) {
                View headerRoot = mSubWorksViewerHeaderBinder.getRootView();
                int posterHeight = headerRoot.getHeight();
                int diff = posterHeight - scrollingWatcher.getScrollOffsetY();

                if (diff < 0) {
                    headerRoot.setVisibility(View.INVISIBLE);
                    return;
                }

                headerRoot.setVisibility(View.VISIBLE);
                float alpha = Math.abs((float) diff / posterHeight);
                headerRoot.setAlpha(alpha);
            }
        });

        mSubWorksViewerSheetBehavior = BottomSheetBehavior.from(root);
        BottomSheetCallbackHelper sheetCallbackHelper = new BottomSheetCallbackHelper();
        sheetCallbackHelper.attachToHost(mSubWorksViewerSheetBehavior);
        sheetCallbackHelper.setStateChangeListener(
                new BottomSheetCallbackHelper.StateChangeListener() {
                    @Override
                    public void onSheetStateChanged(
                            @NonNull BottomSheetCallbackHelper.SheetState state) {

                        switch (state) {
                            case EXPANDED:
                                showBottomSheetScrim(mSubWorksViewerScrim,
                                        mSubWorksViewerHeaderBinder.getCenterOfIcon());
                                mSubWorksViewerHeaderBinder.setPosterModeWithAnim(
                                        HEADER_MODE_TITLE_MESSENGER);
                                break;

                            case EXPANDED_BUT_COULD_COLLAPSED:
                                hideScrimWithAnim(mSubWorksViewerScrim,
                                        mSubWorksViewerHeaderBinder.getCenterOfIcon());
                                mSubWorksViewerHeaderBinder.setPosterModeWithAnim(
                                        HEADER_MODE_NAVIGATION_MESSENGER);
                                break;

                            case COLLAPSED_BUT_COULD_EXPANDED:
                                mSubWorksViewerHeaderBinder.setPosterModeWithAnim(
                                        HEADER_MODE_NAVIGATION_MESSENGER);
                                break;

                            case COLLAPSED:
                            case WILL_COLLAPSED_FROM_TOP:
                                mSubWorksViewerHeaderBinder.setPosterModeWithAnim(
                                        HEADER_MODE_POSTER);
                                break;
                        }
                    }
                });
    }

    private void setupCommentsViewerBottomSheet() {
        ResUtils resUtils = new ResUtils(this);

        View root = findViewById(R.id.fl_comments_viewer_bs_root);
        ViewCompat.setElevation(root, resUtils.getPx(R.dimen.comments_viewer_bs_elevation));

        mCommentsViewerScrim = findViewById(R.id.view_comments_viewer_bs_scrim);

        mCommentsViewerHeaderBinder = new CommentsViewerBsHeaderBinder(
                findViewById(R.id.include_comments_viewer_bs_header));

        RecyclerView commentList = findViewById(R.id.recycler_comments_viewer_bs_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentList.setLayoutManager(layoutManager);
        commentList.setAdapter(mCommentAdapter);

        SpacerItemDecoration spacerItemDecoration = new SpacerItemDecoration();
        spacerItemDecoration.setTopSpace(resUtils.getPx(R.dimen.comments_viewer_bs_list_top_offset));
        spacerItemDecoration.setLeftSpace(resUtils.getPx(R.dimen.comments_viewer_bs_list_side_offset));
        spacerItemDecoration.setRightSpace(resUtils.getPx(R.dimen.comments_viewer_bs_list_side_offset));
        spacerItemDecoration.setBottomSpace(resUtils.getPx(R.dimen.comments_viewer_bs_list_bottom_offset));
        spacerItemDecoration.setSpaceBetweenItems(resUtils.getPx(R.dimen.comments_viewer_bs_list_items_offset));
        commentList.addItemDecoration(spacerItemDecoration);

        // Fade out the header along with scrolling
        ScrollingWatcher scrollingWatcher = new ScrollingWatcher(commentList);
        scrollingWatcher.setCallback(new ScrollingWatcher.Callback() {
            @Override
            public void onScrolled(ScrollingWatcher scrollingWatcher) {
                View headerRoot = mCommentsViewerHeaderBinder.getRootView();
                int posterHeight = headerRoot.getHeight();
                int diff = posterHeight - scrollingWatcher.getScrollOffsetY();

                if (diff < 0) {
                    headerRoot.setVisibility(View.INVISIBLE);
                    return;
                }

                headerRoot.setVisibility(View.VISIBLE);
                float alpha = Math.abs((float) diff / posterHeight);
                headerRoot.setAlpha(alpha);
            }
        });

        mCommentsViewerSheetBehavior = BottomSheetBehavior.from(root);
        BottomSheetCallbackHelper sheetCallbackHelper = new BottomSheetCallbackHelper();
        sheetCallbackHelper.attachToHost(mCommentsViewerSheetBehavior);
        sheetCallbackHelper.setStateChangeListener(
                new BottomSheetCallbackHelper.StateChangeListener() {
                    @Override
                    public void onSheetStateChanged(
                            @NonNull BottomSheetCallbackHelper.SheetState state) {

                        switch (state) {
                            case EXPANDED:
                                showBottomSheetScrim(mCommentsViewerScrim,
                                        mCommentsViewerHeaderBinder.getCenterOfIcon());
                                mCommentsViewerHeaderBinder.setPosterModeWithAnim(
                                        HEADER_MODE_TITLE_MESSENGER);
                                break;

                            case EXPANDED_BUT_COULD_COLLAPSED:
                                hideScrimWithAnim(mCommentsViewerScrim,
                                        mCommentsViewerHeaderBinder.getCenterOfIcon());
                                mCommentsViewerHeaderBinder.setPosterModeWithAnim(
                                        HEADER_MODE_NAVIGATION_MESSENGER);
                                break;

                            case COLLAPSED_BUT_COULD_EXPANDED:
                                mCommentsViewerHeaderBinder.setPosterModeWithAnim(
                                        HEADER_MODE_NAVIGATION_MESSENGER);
                                break;

                            case COLLAPSED:
                            case WILL_COLLAPSED_FROM_TOP:
                                mCommentsViewerHeaderBinder.setPosterModeWithAnim(
                                        HEADER_MODE_POSTER);
                                break;
                        }
                    }
                });
    }

    private void showBottomSheetScrim(View scrim, Point revealPoint) {
        if (scrim.isShown()) return;
        int startRad = 0;
        int endRad = (int) Math.hypot(scrim.getWidth(), scrim.getHeight());
        Animator revealAnim = ViewAnimationUtils
                .createCircularReveal(scrim, revealPoint.x, revealPoint.y, startRad, endRad);
        scrim.setVisibility(View.VISIBLE);
        revealAnim.start();
    }

    private void hideScrimWithAnim(final View scrim, Point hidePoint) {
        if (!scrim.isShown()) return;
        int startRad = (int) Math.hypot(scrim.getWidth(), scrim.getHeight());
        int endRad = 0;
        Animator closeAnim = ViewAnimationUtils
                .createCircularReveal(scrim, hidePoint.x, hidePoint.y, startRad, endRad);
        closeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scrim.setVisibility(View.GONE);
            }
        });
        closeAnim.start();
    }

    private void invalidateBottomSheetTitleMessage() {
        mSubWorksViewerHeaderBinder.setTitleMessage(
                String.valueOf(mSubWorkAdapter.getItemCount()) + " Sub Works");
        mCommentsViewerHeaderBinder.setTitleMessage(
                String.valueOf(mCommentAdapter.getItemCount()) + " Comments");
    }

    /**
     * INTERFACE IMPL -> WorkAdapter.ItemCardClickListener
     * ---------- */

    @Override
    public void onItemCardClick(int position) {

    }

    /* -------------------------------- *
     * MODE OF A BOTTOM SHEET HEADER
     * -------------------------------- */

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HEADER_MODE_INVALID, HEADER_MODE_POSTER,
            HEADER_MODE_NAVIGATION_MESSENGER, HEADER_MODE_TITLE_MESSENGER})
    private @interface HeaderModel {}
    private static final int HEADER_MODE_INVALID = -1;
    private static final int HEADER_MODE_POSTER = 0;
    private static final int HEADER_MODE_NAVIGATION_MESSENGER = 1;
    private static final int HEADER_MODE_TITLE_MESSENGER = 2;
    
    /* ---------------------------------------------------------------------------- *
     * BINDER CLASS FOR THE HEADER OF THE SUB WORKS VIEWER BOTTOM SHEET.
     * THIS CLASS DEPENDS ON {res/layout/header_of_sub_works_viewer_bottom_sheetheet.xml}.
     * ---------------------------------------------------------------------------- */

    private static class SubWorksViewerBsHeaderBinder {
        
        // UI
        private final ViewGroup mRoot;
        private final ImageView mIcon;
        private final TextView mPosterWorkTitle;
        private final TextView mPosterWorkSummary;
        private final TextView mNavigationMessage;
        private final TextView mTitleMessage;

        @HeaderModel
        private int mCurrentPosterMode = HEADER_MODE_INVALID;

        SubWorksViewerBsHeaderBinder(View root) {
            mRoot = (ViewGroup) root;
            mIcon = root.findViewById(R.id.img_header_icon);
            mPosterWorkTitle = root.findViewById(R.id.txt_header_poster_sub_work_title);
            mPosterWorkSummary = root.findViewById(R.id.txt_header_poster_sub_work_summary);
            mNavigationMessage = root.findViewById(R.id.txt_header_navigation_message);
            mTitleMessage = root.findViewById(R.id.txt_header_title_message);

            setHeaderMode(HEADER_MODE_POSTER);
        }
        
        void setHeaderMode(@HeaderModel int mode) {
            setHeaderMode(mode, false);
        }

        void setPosterModeWithAnim(@HeaderModel int mode) {
            setHeaderMode(mode, true);
        }

        void setHeaderMode(@HeaderModel int mode, boolean animate) {
            if (mCurrentPosterMode == mode) return;

            if (animate) {
                AutoTransition transition = new AutoTransition();
                transition.setDuration(250);
                transition.addTarget(mPosterWorkTitle);
                transition.addTarget(mPosterWorkSummary);
                transition.addTarget(mNavigationMessage);
                transition.addTarget(mTitleMessage);
                TransitionManager.beginDelayedTransition(mRoot);
            }

            switch (mode) {
                case HEADER_MODE_POSTER:
                    mPosterWorkTitle.setVisibility(View.VISIBLE);
                    mPosterWorkSummary.setVisibility(View.VISIBLE);
                    mNavigationMessage.setVisibility(View.GONE);
                    mTitleMessage.setVisibility(View.GONE);
                    break;

                case HEADER_MODE_NAVIGATION_MESSENGER:
                    mPosterWorkTitle.setVisibility(View.GONE);
                    mPosterWorkSummary.setVisibility(View.GONE);
                    mNavigationMessage.setVisibility(View.VISIBLE);
                    mTitleMessage.setVisibility(View.GONE);
                    break;

                case HEADER_MODE_TITLE_MESSENGER:
                    mPosterWorkTitle.setVisibility(View.GONE);
                    mPosterWorkSummary.setVisibility(View.GONE);
                    mNavigationMessage.setVisibility(View.GONE);
                    mTitleMessage.setVisibility(View.VISIBLE);
                    break;

                case HEADER_MODE_INVALID:
                    throw new IllegalArgumentException("invalid mode");
            }

            mCurrentPosterMode = mode;
        }

        View getRootView() {
            return mRoot;
        }

        Point getCenterOfIcon() {
            Point point = new Point();
            point.x = mIcon.getLeft() + mIcon.getWidth() / 2;
            point.y = mIcon.getTop() + mIcon.getHeight() / 2;
            return point;
        }

        void setTitleMessage(String message) {
            mTitleMessage.setText(message);
        }
    }

    /* ---------------------------------------------------------------------------- *
     * BINDER CLASS FOR THE HEADER OF THE COMMENTS VIEWER BOTTOM SHEET.
     * THIS CLASS DEPENDS ON {res/layout/header_of_comments_viewer_bottom_sheet.xml}.
     * ---------------------------------------------------------------------------- */

    private static class CommentsViewerBsHeaderBinder {

        // UI
        private final ViewGroup mRoot;
        private final ImageView mIcon;
        private final TextView mPosterCommentTimeLabel;
        private final TextView mPosterCommentText;
        private final TextView mNavigationMessage;
        private final TextView mTitleMessage;

        @HeaderModel
        private int mCurrentPosterMode = HEADER_MODE_INVALID;

        CommentsViewerBsHeaderBinder(View root) {
            mRoot = (ViewGroup) root;
            mIcon = root.findViewById(R.id.img_header_icon);
            mPosterCommentTimeLabel = root.findViewById(R.id.txt_header_poster_comment_time_label);
            mPosterCommentText = root.findViewById(R.id.txt_header_poster_comment_text);
            mNavigationMessage = root.findViewById(R.id.txt_header_navigation_message);
            mTitleMessage = root.findViewById(R.id.txt_header_title_message);

            setHeaderMode(HEADER_MODE_POSTER);
        }

        void setHeaderMode(@HeaderModel int mode) {
            setHeaderMode(mode, false);
        }

        void setPosterModeWithAnim(@HeaderModel int mode) {
            setHeaderMode(mode, true);
        }

        void setHeaderMode(@HeaderModel int mode, boolean animate) {
            if (mCurrentPosterMode == mode) return;

            if (animate) {
                AutoTransition transition = new AutoTransition();
                transition.setDuration(250);
                transition.addTarget(mPosterCommentTimeLabel);
                transition.addTarget(mPosterCommentText);
                transition.addTarget(mNavigationMessage);
                transition.addTarget(mTitleMessage);
                TransitionManager.beginDelayedTransition(mRoot);
            }

            switch (mode) {
                case HEADER_MODE_POSTER:
                    mPosterCommentTimeLabel.setVisibility(View.VISIBLE);
                    mPosterCommentText.setVisibility(View.VISIBLE);
                    mNavigationMessage.setVisibility(View.GONE);
                    mTitleMessage.setVisibility(View.GONE);
                    break;

                case HEADER_MODE_NAVIGATION_MESSENGER:
                    mPosterCommentTimeLabel.setVisibility(View.GONE);
                    mPosterCommentText.setVisibility(View.GONE);
                    mNavigationMessage.setVisibility(View.VISIBLE);
                    mTitleMessage.setVisibility(View.GONE);
                    break;

                case HEADER_MODE_TITLE_MESSENGER:
                    mPosterCommentTimeLabel.setVisibility(View.GONE);
                    mPosterCommentText.setVisibility(View.GONE);
                    mNavigationMessage.setVisibility(View.GONE);
                    mTitleMessage.setVisibility(View.VISIBLE);
                    break;

                case HEADER_MODE_INVALID:
                    throw new IllegalArgumentException("invalid mode");
            }

            mCurrentPosterMode = mode;
        }

        View getRootView() {
            return mRoot;
        }

        Point getCenterOfIcon() {
            Point point = new Point();
            point.x = mIcon.getLeft() + mIcon.getWidth() / 2;
            point.y = mIcon.getTop() + mIcon.getHeight() / 2;
            return point;
        }

        void setTitleMessage(String message) {
            mTitleMessage.setText(message);
        }
    }
}
