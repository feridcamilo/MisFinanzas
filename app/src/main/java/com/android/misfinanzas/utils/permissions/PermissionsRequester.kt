package com.android.misfinanzas.utils.permissions

import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.fragment.app.Fragment
import com.android.misfinanzas.R
import com.android.misfinanzas.utils.showRationaleDialog

class PermissionsRequester(
    private val fragment: Fragment,
    private val permissions: Array<Permission>
) {

    private var onGranted: () -> Unit = {}

    private val requestPermission =
        fragment.registerForActivityResult(RequestMultiplePermissions()) { handleResult(it) }

    fun runWithPermissions(onGranted: () -> Unit) {
        this.onGranted = onGranted
        requestPermissions()
    }

    private fun handleResult(permissionsMap: Map<String, Boolean>) {
        if (permissionsMap.any { !it.value }) {
            permissionsMap
                .filter { !it.value }
                .mapNotNull { findPermission(it.key) }
                .filter { shouldShowRequestPermissionRationale(fragment.requireActivity(), it.key) }
                .map { it.messageResId }
                .let { buildRationale(it) }
                .also { showRationale(it) }
        } else {
            onGranted.invoke()
        }
    }

    private fun showRationale(rationale: Pair<String, String>) {
        val (title, message) = rationale
        if (message.isNotEmpty()) {
            fragment.context?.showRationaleDialog(title, message) {
                requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        requestPermission.launch(permissions.map { it.key }.toTypedArray())
    }

    private fun findPermission(key: String): Permission? {
        return permissions.find { it.key == key }
    }

    private fun buildRationale(resIds: List<Int>): Pair<String, String> {
        val title = fragment.resources.getQuantityString(R.plurals.rationale_title, resIds.size)
        val message = resIds.map { fragment.getString(it) }.joinToString("\n\n") { it }
        return Pair(title, message)
    }

}
