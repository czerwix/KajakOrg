package com.mobeedev.kajakorg.data.model.detail

/**
 * This enum contains values of different events you can find on your way.
 * the specified names of bridges,Weir etc are names very specific to Polish language,
 * thats wehy i have left them in polish. might attempt to translate these values in teh future.
 */
enum class EventType(val value: String) {
    Biwak("biwak"),
    Jaz("jaz"),
    Most("most"),
    Przenoska("przenoska"),
    Sklep("sklep"),
    Stanica("stanica"),
    Ujscie("ujście"),
    Uwaga("uwaga"),
    Wyplyw("wypływ");

    companion object {
        public fun fromValue(value: String): EventType = when (value) {
            "biwak"     -> Biwak
            "jaz"       -> Jaz
            "most"      -> Most
            "przenoska" -> Przenoska
            "sklep"     -> Sklep
            "stanica"   -> Stanica
            "ujście"    -> Ujscie
            "uwaga"     -> Uwaga
            "wypływ"    -> Wyplyw
            else        -> throw error("Unrecognised pathType")
        }
    }
}
