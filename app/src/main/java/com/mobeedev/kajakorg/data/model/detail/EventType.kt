package com.mobeedev.kajakorg.data.model.detail

import com.mobeedev.kajakorg.R

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

    fun getIconResId() = when (this) {
        Biwak -> R.drawable.campsite_icon
        Jaz -> R.drawable.weir_icon
        Most -> R.drawable.bridge_icon
        Przenoska -> R.drawable.carry_icon
        Sklep -> R.drawable.shop_icon
        Stanica -> R.drawable.hostel_icon
        Ujscie -> R.drawable.information_icon
        Uwaga -> R.drawable.warning_icon
        Wyplyw -> R.drawable.information_icon
        Wypozyczalnia -> R.drawable.rent_icon
        Niebezpieczeństwo -> R.drawable.danger_icon
        Lekarz -> R.drawable.doctor_icon
        Bar -> R.drawable.bar_icon
        sluza -> R.drawable.information_icon
    }

    companion object {
        fun fromValue(value: String): EventType = when (value) {
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
