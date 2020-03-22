package com.shaji.kotlina.framework.generic

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.shaji.kotlina.view.MaterialDialog

typealias Callback<T> = (T) -> Unit
typealias Success<T> = (T) -> Unit
typealias SuccessNullable<T> = (T?) -> Unit

typealias Failure = (String) -> Unit
typealias FailureObject<T> = (T) -> Unit

typealias VoidListener = () -> Unit
typealias OnDialogClickListener = (dialog: MaterialDialog, id: Int) -> Boolean

typealias PermissionRequestCallback = (granted: Boolean, permanentlyDenied: Boolean) -> Unit
typealias UpdateAvailableListener = (appUpdateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo) -> Unit
