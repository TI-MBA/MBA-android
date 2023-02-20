package com.gabia.mbaproject.model.enums

enum class Instrument(val value: String) {
    AGBE("AGBE"),
    AGOGO("AGOGO"),
    GONGUE("GONGUE"),
    CAIXA("CAIXA"),
    ALFAIA("ALFAIA"),
    CANTO("CANTO"),
    MAESTRIA("MAESTRIA");

    val formattedValue: String
        get() {
            var result = ""
            result = when (value) {
                "AGBE" -> "Agbê"
                "AGOGO" -> "Agogô"
                "GONGUE" -> "Gonguê"
                "CAIXA" -> "Caixa"
                "ALFAIA" -> "Alfaia"
                "CANTO" -> "Canto"
                "MAESTRIA" -> "Maestria"
                else -> "Indefinido"
            }
            return result
        }

    val position: Int
        get() {
            return when (value) {
                "AGBE" -> 0
                "AGOGO" -> 1
                "GONGUE" -> 2
                "CAIXA" -> 3
                "ALFAIA" -> 4
                "CANTO" -> 5
                "MAESTRIA" -> 6
                else -> 0
            }
        }
}