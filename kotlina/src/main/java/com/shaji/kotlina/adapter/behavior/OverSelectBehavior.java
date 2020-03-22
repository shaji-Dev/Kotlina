package com.shaji.kotlina.adapter.behavior;

import androidx.annotation.IntDef;

import com.shaji.kotlina.adapter.base.BaseMultiSelectAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({BaseMultiSelectAdapter.OVERSELECT_BEHAVIOR_NONE,
        BaseMultiSelectAdapter.OVERSELECT_BEHAVIOR_REMOVE_FIRST,
        BaseMultiSelectAdapter.OVERSELECT_BEHAVIOR_REMOVE_LAST})
public @interface OverSelectBehavior { }
