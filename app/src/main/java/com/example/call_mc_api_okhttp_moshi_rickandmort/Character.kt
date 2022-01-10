package com.example.call_mc_api_okhttp_moshi_rickandmort

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)


data class CharactersAll(
    val info: Info,
    val results: List<Character>
)

data class Info(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any?
)




data class Character(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

data class Location(
    val name: String,
    val url: String
)

data class Origin(
    val name: String,
    val url: String
)