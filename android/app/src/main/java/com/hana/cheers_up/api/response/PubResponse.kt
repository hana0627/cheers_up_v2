package com.hana.cheers_up.api.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PubResponse(
    val pubName: String,
    val pubAddress: String,
    val directionUrl: String,
    val roadViewUrl: String,
    val roadView: String,
    val categoryName: String,
    val distance: String
) : Parcelable