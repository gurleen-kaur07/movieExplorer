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
    NO_DATA("Data not available to display"),
    PAGE_END("No more data to load"),
    ERROR("Failed to load more data"),
}
