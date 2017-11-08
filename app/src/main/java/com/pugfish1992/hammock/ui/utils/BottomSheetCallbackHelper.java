package com.pugfish1992.hammock.ui.utils;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;

import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by daichi on 11/7/17.
 */

public class BottomSheetCallbackHelper {

    public interface StateChangeListener {
        /**
         * Called when the bottom sheet changes its state.
         */
        void onSheetStateChanged(int tag, @NonNull SheetState state);
    }

    public enum SheetState {
        SLIDING_BETWEEN_COLLAPSED_EXPANDED,
        SLIDING_BETWEEN_COLLAPSED_HIDDEN,

        EXPANDED,
        WILL_EXPANDED,
        EXPANDED_BUT_COULD_COLLAPSED,

        HIDDEN,
        WILL_HIDDEN,
        HIDDEN_BUT_COULD_COLLAPSED,

        COLLAPSED,
        WILL_COLLAPSED_FROM_TOP,
        WILL_COLLAPSED_FROM_BOTTOM,
        COLLAPSED_BUT_COULD_EXPANDED,
        COLLAPSED_BUT_COULD_HIDDEN
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STABLE_STATE_EXPANDED, STABLE_STATE_COLLAPSED, STABLE_STATE_HIDDEN, STABLE_STATE_UNSTABLE})
    @interface StableBehaviorState{}
    private static final int STABLE_STATE_EXPANDED = 0;
    private static final int STABLE_STATE_COLLAPSED = 1;
    private static final int STABLE_STATE_HIDDEN = 2;
    private static final int STABLE_STATE_UNSTABLE = 3;

    private static final float OFFSET_OF_STATE_COLLAPSED = 0f;
    private static final float OFFSET_OF_STATE_EXPANDED = 1f;
    private static final float OFFSET_OF_STATE_HIDDEN = -1f;

    // Thresholds
    private static final float THOLD_NEAR_EXPAND = 0.8f;
    private static final float THOLD_NEAR_HIDE = -0.8f;
    private static final float THOLD_NEAR_COLLAPSE_TOP = 0.2f;
    private static final float THOLD_NEAR_COLLAPSE_BOTTOM = -0.2f;

    private int mTag;
    private BottomSheetBehavior mHost;
    private SheetState mPrevSheetState;
    @StableBehaviorState
    private int mPrevStableBehaviorState;
    private StateChangeListener mStateChangeListener;

    public final void attachToHost(BottomSheetBehavior host) {
        attachToHost(host, 0);
    }

    public final void attachToHost(BottomSheetBehavior host, int tag) {
        mTag = tag;

        if (mHost != null) {
            mHost.setBottomSheetCallback(null);
        }
        mHost = host;
        if (host != null) {
            host.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    BottomSheetCallbackHelper.this.onStateChanged(newState);
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    BottomSheetCallbackHelper.this.onSlide(slideOffset);
                }
            });

            switch (host.getState()) {
                case BottomSheetBehavior.STATE_COLLAPSED:
                    mPrevStableBehaviorState = STABLE_STATE_COLLAPSED;
                    mPrevSheetState = SheetState.COLLAPSED;
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    mPrevStableBehaviorState = STABLE_STATE_EXPANDED;
                    mPrevSheetState = SheetState.EXPANDED;
                    break;
                case BottomSheetBehavior.STATE_HIDDEN:
                    mPrevStableBehaviorState = STABLE_STATE_HIDDEN;
                    mPrevSheetState = SheetState.HIDDEN;
                    break;
                default:
                    throw new IllegalStateException(
                            "A state of the passed bottom sheet should be one of " +
                                    "STATE_COLLAPSED, STATE_EXPANDED, STATE_HIDDEN.");
            }
        }
    }

    public final void setStateChangeListener(StateChangeListener stateChangeListener) {
        mStateChangeListener = stateChangeListener;
    }

    private void onStateChanged(int newState) {
        switch (newState) {
            case BottomSheetBehavior.STATE_EXPANDED:
                mPrevStableBehaviorState = STABLE_STATE_EXPANDED;
                onNextSheetStateConfirmed(SheetState.EXPANDED);
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                mPrevStableBehaviorState = STABLE_STATE_COLLAPSED;
                onNextSheetStateConfirmed(SheetState.COLLAPSED);
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                mPrevStableBehaviorState = STABLE_STATE_HIDDEN;
                onNextSheetStateConfirmed(SheetState.HIDDEN);
                break;
        }
    }

    private void onSlide(float slideOffset) {
        SheetState nextState = null;

        if (OFFSET_OF_STATE_COLLAPSED < slideOffset && slideOffset < OFFSET_OF_STATE_EXPANDED) {
            // Between collapsed and expanded

            if (THOLD_NEAR_EXPAND < slideOffset) {
                nextState = (mPrevStableBehaviorState == STABLE_STATE_EXPANDED)
                        ? SheetState.EXPANDED_BUT_COULD_COLLAPSED
                        : SheetState.WILL_EXPANDED;
            } else
            if (THOLD_NEAR_COLLAPSE_TOP < slideOffset) {
                nextState = SheetState.SLIDING_BETWEEN_COLLAPSED_EXPANDED;
                mPrevStableBehaviorState = STABLE_STATE_UNSTABLE;
            } else {
                nextState = (mPrevStableBehaviorState == STABLE_STATE_COLLAPSED)
                        ? SheetState.COLLAPSED_BUT_COULD_EXPANDED
                        : SheetState.WILL_COLLAPSED_FROM_TOP;
            }

        } else if (OFFSET_OF_STATE_HIDDEN < slideOffset && slideOffset < OFFSET_OF_STATE_COLLAPSED) {
            // Between hidden and collapsed

            if (THOLD_NEAR_COLLAPSE_BOTTOM < slideOffset) {
                nextState = (mPrevStableBehaviorState == STABLE_STATE_COLLAPSED)
                        ? SheetState.COLLAPSED_BUT_COULD_HIDDEN
                        : SheetState.WILL_COLLAPSED_FROM_BOTTOM;
            } else
            if (THOLD_NEAR_HIDE < slideOffset) {
                nextState = SheetState.SLIDING_BETWEEN_COLLAPSED_HIDDEN;
                mPrevStableBehaviorState = STABLE_STATE_UNSTABLE;
            } else {
                nextState = (mPrevStableBehaviorState == STABLE_STATE_HIDDEN)
                        ? SheetState.HIDDEN_BUT_COULD_COLLAPSED
                        : SheetState.WILL_HIDDEN;
            }
        }

        onNextSheetStateConfirmed(nextState);
    }

    private void onNextSheetStateConfirmed(@Nullable SheetState nextState) {
        if (nextState != null && nextState != mPrevSheetState) {
            mPrevSheetState = nextState;
            if (mStateChangeListener != null) {
                mStateChangeListener.onSheetStateChanged(mTag, nextState);
            }
        }
    }
}
