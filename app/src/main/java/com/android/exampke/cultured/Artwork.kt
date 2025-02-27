package com.android.exampke.cultured

data class Artwork(
    val artist: Artist = Artist(),
    val title: String = "",
    val productionYear: String = "",
    val material: String = "",
    val artType: String = "",
    val medium: String = "",
    val location: Location = Location(),
    val description: String = "",
    val imageUrl: String = ""
)

data class Artist(
    val name: String = "",
    val nationality: String = "",
    val birthYear: Int = 0,
    val deathYear: Int = 0
)

data class Location(
    val museum: String = "",
    val city: String = "",
    val country: String = ""
)
