package com.example.denysrandy_comp304002_lab04.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents an NBA Division
 * @param id Unique identifier for the division
 * @param name Display name of the division
 * @param conference The conference this division belongs to (Eastern or Western)
 * @param description Brief description of the division
 */
@Parcelize
data class NBADivision(
    val id: String,
    val name: String,
    val conference: String,
    val description: String
) : Parcelable

/**
 * Provides all NBA divisions
 */
object NBADivisions {
    val allDivisions = listOf(
        NBADivision(
            id = "atlantic",
            name = "Atlantic Division",
            conference = "Eastern Conference",
            description = "Boston, Brooklyn, New York, Philadelphia, Toronto"
        ),
        NBADivision(
            id = "central",
            name = "Central Division",
            conference = "Eastern Conference",
            description = "Chicago, Cleveland, Detroit, Indiana, Milwaukee"
        ),
        NBADivision(
            id = "southeast",
            name = "Southeast Division",
            conference = "Eastern Conference",
            description = "Atlanta, Charlotte, Miami, Orlando, Washington"
        ),
        NBADivision(
            id = "northwest",
            name = "Northwest Division",
            conference = "Western Conference",
            description = "Denver, Minnesota, Oklahoma City, Portland, Utah"
        ),
        NBADivision(
            id = "pacific",
            name = "Pacific Division",
            conference = "Western Conference",
            description = "Golden State, LA Clippers, LA Lakers, Phoenix, Sacramento"
        ),
        NBADivision(
            id = "southwest",
            name = "Southwest Division",
            conference = "Western Conference",
            description = "Dallas, Houston, Memphis, New Orleans, San Antonio"
        )
    )
}
