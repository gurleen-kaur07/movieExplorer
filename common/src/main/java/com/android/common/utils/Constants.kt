package com.android.common.utils

enum class Constants(
    val get: String,
) {
    MOVIE_LANG("en-US/"),
}

enum class Pagination(
    val page: Int,
) {
    INIT_PAGE(1),
}

enum class DataStates(
    val message: String,
) {
    PAGE_END("No more data to load"),
}
