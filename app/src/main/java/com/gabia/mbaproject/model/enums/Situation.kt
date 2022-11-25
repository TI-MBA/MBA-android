package com.gabia.mbaproject.model.enums

import com.gabia.mbaproject.R

enum class Situation(val value: String) {
    UP_TO_DATE("UP_TO_DATE"),
    DEBIT("DEBIT"),
    EXEMPT("EXEMPT"),
    AGREEMENT("AGREEMENT");

    val formattedValue: String
        get() {
            var result = ""
            result = when (value) {
                "UP_TO_DATE" -> "Em dia"
                "DEBIT" -> "Débito"
                "EXEMPT" -> "Isento"
                "AGREEMENT" -> "Em acordo"
                else -> "Indefinido"
            }
            return result
        }

    val situationColor: Int
        get() {
            var result = R.color.gray
            result = when (value) {
                "UP_TO_DATE" -> R.color.successGreen
                "DEBIT" -> R.color.baque_red
                "EXEMPT" -> R.color.baque_blue
                "AGREEMENT" -> R.color.yellow_warning
                else -> R.color.gray
            }
            return result
        }

    val position: Int
        get() {
            var result = R.color.gray
            result = when (value) {
                "UP_TO_DATE" -> 0
                "DEBIT" -> 1
                "EXEMPT" -> 2
                "AGREEMENT" -> 3
                else -> 0
            }
            return result
        }
}