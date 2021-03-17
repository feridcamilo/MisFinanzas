package com.android.misfinanzas.utils.permissions

import android.Manifest
import androidx.annotation.StringRes
import com.android.misfinanzas.R

sealed class Permission(
    @StringRes val messageResId: Int,
    val key: String
) {

    class Read(
        @StringRes messageResId: Int = R.string.read_permission_message,
        key: String = Manifest.permission.READ_EXTERNAL_STORAGE
    ) : Permission(messageResId, key)

    class Write(
        @StringRes messageResId: Int = R.string.write_permission_message,
        key: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) : Permission(messageResId, key)

    class Camera(
        @StringRes messageResId: Int = R.string.camera_permission_message,
        key: String = Manifest.permission.CAMERA
    ) : Permission(messageResId, key)

    class LocationFine(
        @StringRes messageResId: Int = R.string.location_permission_message,
        key: String = Manifest.permission.ACCESS_FINE_LOCATION
    ) : Permission(messageResId, key)

    class LocationCoarse(
        @StringRes messageResId: Int = R.string.location_permission_message,
        key: String = Manifest.permission.ACCESS_COARSE_LOCATION
    ) : Permission(messageResId, key)

    class SmsRead(
        @StringRes messageResId: Int = R.string.sms_permission_message,
        key: String = Manifest.permission.READ_SMS
    ) : Permission(messageResId, key)

}
