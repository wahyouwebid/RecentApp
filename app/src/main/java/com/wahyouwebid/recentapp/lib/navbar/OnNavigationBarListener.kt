package com.wahyouwebid.recentapp.lib.navbar

/**
 * copy from https://github.com/gyf-dev/ImmersionBar.
 * The interface On navigation bar listener.
 **/

fun interface OnNavigationBarListener {
    /**
     * On navigation bar change.
     *
     * @param isGestureEnabled
     */
    fun onNavigationBarChange(isGestureEnabled: Boolean)
}
