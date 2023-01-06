package com.mobeedev.kajakorg.data.model.detail

/**
 * This enum contains values of different events you can find on your way.
 * the specified names of bridges,Weir etc are names very specific to Polish language,
 * thats why i have left them in polish. might attempt to translate these values in the future.
 */
enum class EventType(val polishName: String) {
    Biwak("biwak"),
    Jaz("jaz"),
    Most("most"),
    Przenoska("przenoska"),
    Sklep("sklep"),
    Stanica("stanica"),
    Ujscie("ujście"),
    Uwaga("uwaga"),
    Wyplyw("wypływ"),
    Wypozyczalnia("wypożyczalnia"),
    Niebezpieczeństwo("niebezpieczeństwo"),
    Lekarz("lekarz"),
    Bar("bar"),
    sluza("śluza");

    companion object {
        public fun fromValue(value: String): EventType = when (value) {
            "biwak" -> Biwak
            "jaz" -> Jaz
            "most" -> Most
            "przenoska" -> Przenoska
            "sklep" -> Sklep
            "stanica" -> Stanica
            "ujście" -> Ujscie
            "uwaga" -> Uwaga
            "wypływ" -> Wyplyw
            "wypożyczalnia" -> Wypozyczalnia
            "niebezpieczeństwo" -> Niebezpieczeństwo
            "lekarz" -> Lekarz
            "bar" -> Bar
            "śluza" -> sluza
            else -> throw error("Unrecognised Event type: $value")
        }
    }
}
