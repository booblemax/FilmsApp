package com.example.filmsapp.util

import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

object Versions {

    fun isApi(api: Int) = VERSION.SDK_INT == api

    fun isApiOrUp(api: Int) = VERSION.SDK_INT >= api

    fun isLollipop() = isApi(VERSION_CODES.LOLLIPOP)
    fun isMarshmallow() = isApi(VERSION_CODES.M)
    fun isNougat() = isApi(VERSION_CODES.N)
    fun isOreo() = isApi(VERSION_CODES.O)

    fun isLollipopOrUp() = isApiOrUp(VERSION_CODES.LOLLIPOP)
    fun isMarshmallowOrUp() = isApiOrUp(VERSION_CODES.M)
    fun isNougatOrUp() = isApiOrUp(VERSION_CODES.N)
    fun isOreoOrUp() = isApiOrUp(VERSION_CODES.O)

    inline fun aboveApi(api: Int, included: Boolean = false, functionBlock: () -> Unit) {
        if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
            functionBlock()
        }
    }

    inline fun belowApi(api: Int, included: Boolean = false, functionBlock: () -> Unit) {
        if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
            functionBlock()
        }
    }
}
