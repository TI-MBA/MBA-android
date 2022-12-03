package com.gabia.mbaproject.model.enums

import com.gabia.mbaproject.R

enum class PresenceType(val value: String) {
    PRESENT("PRESENT"),
    ABSENT("ABSENT"),
    OBSERVATION("OBSERVATION");

    val presenceIcon: Int
        get() {
            var result = R.color.gray
            result = when (value) {
                "PRESENT" -> R.drawable.ic_presence_green
                "ABSENT" -> R.drawable.ic_absence_red
                "OBSERVATION" -> R.drawable.ic_observation_yellow
                else -> R.drawable.ic_undefined_gray
            }
            return result
        }
}