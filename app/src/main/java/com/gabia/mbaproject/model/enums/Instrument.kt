package com.gabia.mbaproject.model.enums

enum class Instrument(val value: String) {
    AGBE("AGBE"),
    AGOGO("AGOGO"),
    GONGUE("GONGUE"),
    CAIXA("CAIXA"),
    ALFAIA("ALFAIA"),
    CANTO("CANTO");

    val formattedValue: String
        get() {
            var result = ""
            result = when (value) {
                "AGBE" -> "Agbe"
                "AGOGO" -> "Agogo"
                "GONGUE" -> "Gonguê"
                "CAIXA" -> "Caixa"
                "ALFAIA" -> "Alfaia"
                "CANTO" -> "Canto"
                else -> "Indefinido"
            }
            return result
        }
}