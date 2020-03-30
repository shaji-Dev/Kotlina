package com.shaji.kotlina.extension.generic

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.shaji.kotlina.R
import com.shaji.kotlina.framework.generic.PermissionRequestCallback
import com.shaji.kotlina.framework.generic.Success
import com.shaji.kotlina.framework.generic.UpdateAvailableListener
import com.shaji.kotlina.framework.generic.VoidListener
import com.shaji.kotlina.view.dialog.MaterialDialog
import org.jetbrains.anko.toast

fun Activity.inflate(@LayoutRes layoutId: Int, parent: ViewGroup? = null): View {
    return layoutInflater.inflate(layoutId, parent)
}

inline fun <reified T : Activity> Activity.startActivityIfNeeded(vararg params: Pair<String, Any?>) {
    startActivityIfNeeded(createIntent<T>(*params), 0)
}

inline fun <reified T : Activity> Activity.startActivityIfNeeded(extras: Bundle) {
    startActivityIfNeeded(createIntent<T>(extras), 0)
}

fun Activity.showSoftKeyboard(editText: View?) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideSoftKeyboard() {
    currentFocus?.let { cf ->
        hideSoftKeyboard(cf.windowToken)
    }
}

fun Activity.noTransition() {
    overridePendingTransition(0, 0)
}

fun Activity.getRootView(): View? {
    return window.decorView.rootView
}

fun Activity.requestLocationPermission(requestCode: Int, listener: Success<Boolean>) {
    val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    requestPermission(*permissions) { granted, permenentlyDenied ->
        listener(granted)
        if (!granted) {
            toast(R.string.permission_write_external_storage_rationale)
            if (permenentlyDenied) {
                showSettings(requestCode)
            }
        }
    }
}

fun Activity.requestFilesPermission(requestCode: Int, listener: Success<Boolean>) {
    val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    requestPermission(permission) { granted, permenentlyDenied ->
        listener(granted)
        if (!granted) {
            toast(R.string.permission_write_external_storage_rationale)
            if (permenentlyDenied) {
                showSettings(requestCode)
            }
        }
    }
}

fun Activity.requestPermission(vararg permissions: String, listener: PermissionRequestCallback) {
    if (checkPermissions(*permissions)) {
        listener(true, false)
    } else {
        Dexter.withActivity(this)
                .withPermissions(*permissions)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            listener(it.areAllPermissionsGranted(), false)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        listener(false, true)
                    }
                }).check()
    }
}

fun Activity.showSettings(requestCode: Int) {
    showSettings(packageName, requestCode)
}

fun Activity.showSettings(packageName: String, requestCode: Int) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.parse("package:$packageName")
    startActivityForResult(intent, requestCode)
}

fun Activity.checkIfUpdateAvailable(@AppUpdateType type: Int = AppUpdateType.IMMEDIATE, callback: UpdateAvailableListener) {
    val appUpdateManager = AppUpdateManagerFactory.create(this)

    appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
        Log.d("Update Checker", "UPDATE Info: ${appUpdateInfo.updateAvailability()}   ${appUpdateInfo.installStatus()} ${appUpdateInfo.availableVersionCode()}")
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(type)) {
            callback(appUpdateManager, appUpdateInfo)
        }
    }
}

fun Activity.startAppUpdate(appUpdateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo, @AppUpdateType type: Int, requestCode: Int) {
    if (type == AppUpdateType.FLEXIBLE) {
        val listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate(appUpdateManager, Color.WHITE)
            }
        }
        appUpdateManager.registerListener(listener)
    }

    appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            type,
            this,
        requestCode)
}


/* Displays the snackbar notification and call to action. */
private fun Activity.popupSnackbarForCompleteUpdate(appUpdateManager: AppUpdateManager, textColor: Int) {
    getRootView()?.let {
        val snackbar = Snackbar.make(it,
                R.string.update_downloaded,
                Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.restart) { appUpdateManager.completeUpdate() }
        snackbar.setActionTextColor(textColor)
        snackbar.show()
    }
}

fun Activity.updateNowIfRequired(requestCode: Int) {
    checkIfUpdateAvailable { appUpdateManager, appUpdateInfo ->
        startAppUpdate(appUpdateManager, appUpdateInfo, AppUpdateType.IMMEDIATE, requestCode)
    }
}

fun Activity.showDialog(@StringRes titleId: Int,
                        @StringRes messageId: Int,
                        @StringRes primaryButtonText: Int,
                        @StringRes secondaryButtonText: Int,
                        primaryButtonListener: VoidListener? = null,
                        secondaryButtonListener: VoidListener? = null): MaterialDialog {
    return showDialog(titleId,
            messageId,
            primaryButtonText,
            secondaryButtonText,
            primaryButtonListener,
            secondaryButtonListener,
            true)
}

fun Activity.showErrorDialog(@StringRes titleId: Int,
                             @StringRes messageId: Int,
                             @StringRes primaryButtonText: Int,
                             @StringRes secondaryButtonText: Int,
                             primaryButtonListener: VoidListener? = null,
                             secondaryButtonListener: VoidListener? = null): MaterialDialog {
    return showDialog(titleId,
            messageId,
            primaryButtonText,
            secondaryButtonText,
            primaryButtonListener,
            secondaryButtonListener,
            false)
}

private fun Activity.showDialog(@StringRes titleId: Int,
                                @StringRes messageId: Int,
                                @StringRes primaryButtonText: Int,
                                @StringRes secondaryButtonText: Int,
                                primaryButtonListener: VoidListener? = null,
                                secondaryButtonListener: VoidListener? = null,
                                isPositive: Boolean): MaterialDialog {
    val builder = MaterialDialog.Builder(this)
            .setTitle(titleId)
            .setMessage(messageId)
            .setCancelButton(secondaryButtonText) { dialog, id ->
                if (secondaryButtonListener != null) {
                    secondaryButtonListener()
                }
                true
            }
    if (isPositive) {
        builder.setPositiveButton(primaryButtonText) { dialog, id ->
            if (primaryButtonListener != null) {
                primaryButtonListener()
            }
            true
        }
    } else {
        builder.setNegativeButton(primaryButtonText) { dialog, id ->
            if (primaryButtonListener != null) {
                primaryButtonListener()
            }
            true
        }
    }
    val dialog = builder.create()
    dialog.show()
    return dialog
}

fun Activity.openGooglePlayServices() {
    val appPackageName = packageName
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
    } catch (e: Exception) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")))
    }
}

fun Activity.getColorPrimary(ctx: Activity): Int {
    return getThemeColor(ctx, R.attr.colorPrimary)
}

fun Activity.getColorAccent(ctx: Activity): Int {
    return getThemeColor(ctx, R.attr.colorAccent)
}

private fun Activity.getThemeColor(
    context: Activity,
    @AttrRes attributeColor: Int
): Int {
    val value = TypedValue()
    context.theme.resolveAttribute(attributeColor, value, true)
    return value.data
}