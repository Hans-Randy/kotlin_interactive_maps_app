package com.example.denysrandy_comp304002_lab04.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

/**
 * Represents an NBA Team with arena location information
 * @param id Unique identifier for the team
 * @param name Team name
 * @param city City where the team is located
 * @param arenaName Name of the team's home arena
 * @param arenaLocation GPS coordinates of the arena
 * @param divisionId ID of the division this team belongs to
 * @param logoEmoji Emoji representation of the team
 */
@Parcelize
data class NBATeam(
    val id: String,
    val name: String,
    val city: String,
    val arenaName: String,
    val arenaLocation: LatLng,
    val divisionId: String,
    val logoEmoji: String,
    val founded: Int
) : Parcelable

/**
 * Repository for all NBA teams with their arena locations
 */
object NBATeamsRepository {
    
    // Atlantic Division
    private val atlanticTeams = listOf(
        NBATeam(
            id = "celtics",
            name = "Boston Celtics",
            city = "Boston, MA",
            arenaName = "TD Garden",
            arenaLocation = LatLng(42.366303, -71.062228),
            divisionId = "atlantic",
            logoEmoji = "🍀",
            founded = 1946
        ),
        NBATeam(
            id = "nets",
            name = "Brooklyn Nets",
            city = "Brooklyn, NY",
            arenaName = "Barclays Center",
            arenaLocation = LatLng(40.682661, -73.975225),
            divisionId = "atlantic",
            logoEmoji = "🏀",
            founded = 1967
        ),
        NBATeam(
            id = "knicks",
            name = "New York Knicks",
            city = "New York, NY",
            arenaName = "Madison Square Garden",
            arenaLocation = LatLng(40.750504, -73.993439),
            divisionId = "atlantic",
            logoEmoji = "🗽",
            founded = 1946
        ),
        NBATeam(
            id = "76ers",
            name = "Philadelphia 76ers",
            city = "Philadelphia, PA",
            arenaName = "Wells Fargo Center",
            arenaLocation = LatLng(39.901176, -75.171925),
            divisionId = "atlantic",
            logoEmoji = "🔔",
            founded = 1946
        ),
        NBATeam(
            id = "raptors",
            name = "Toronto Raptors",
            city = "Toronto, ON",
            arenaName = "Scotiabank Arena",
            arenaLocation = LatLng(43.643466, -79.379180),
            divisionId = "atlantic",
            logoEmoji = "🦖",
            founded = 1995
        )
    )
    
    // Central Division
    private val centralTeams = listOf(
        NBATeam(
            id = "bulls",
            name = "Chicago Bulls",
            city = "Chicago, IL",
            arenaName = "United Center",
            arenaLocation = LatLng(41.880695, -87.674176),
            divisionId = "central",
            logoEmoji = "🐂",
            founded = 1966
        ),
        NBATeam(
            id = "cavaliers",
            name = "Cleveland Cavaliers",
            city = "Cleveland, OH",
            arenaName = "Rocket Mortgage FieldHouse",
            arenaLocation = LatLng(41.496483, -81.688047),
            divisionId = "central",
            logoEmoji = "⚔️",
            founded = 1970
        ),
        NBATeam(
            id = "pistons",
            name = "Detroit Pistons",
            city = "Detroit, MI",
            arenaName = "Little Caesars Arena",
            arenaLocation = LatLng(42.341083, -83.055031),
            divisionId = "central",
            logoEmoji = "🏭",
            founded = 1941
        ),
        NBATeam(
            id = "pacers",
            name = "Indiana Pacers",
            city = "Indianapolis, IN",
            arenaName = "Gainbridge Fieldhouse",
            arenaLocation = LatLng(39.763954, -86.155535),
            divisionId = "central",
            logoEmoji = "🏎️",
            founded = 1967
        ),
        NBATeam(
            id = "bucks",
            name = "Milwaukee Bucks",
            city = "Milwaukee, WI",
            arenaName = "Fiserv Forum",
            arenaLocation = LatLng(43.045021, -87.917175),
            divisionId = "central",
            logoEmoji = "🦌",
            founded = 1968
        )
    )
    
    // Southeast Division
    private val southeastTeams = listOf(
        NBATeam(
            id = "hawks",
            name = "Atlanta Hawks",
            city = "Atlanta, GA",
            arenaName = "State Farm Arena",
            arenaLocation = LatLng(33.757222, -84.396389),
            divisionId = "southeast",
            logoEmoji = "🦅",
            founded = 1946
        ),
        NBATeam(
            id = "hornets",
            name = "Charlotte Hornets",
            city = "Charlotte, NC",
            arenaName = "Spectrum Center",
            arenaLocation = LatLng(35.225069, -80.839233),
            divisionId = "southeast",
            logoEmoji = "🐝",
            founded = 1988
        ),
        NBATeam(
            id = "heat",
            name = "Miami Heat",
            city = "Miami, FL",
            arenaName = "Kaseya Center",
            arenaLocation = LatLng(25.781389, -80.188056),
            divisionId = "southeast",
            logoEmoji = "🔥",
            founded = 1988
        ),
        NBATeam(
            id = "magic",
            name = "Orlando Magic",
            city = "Orlando, FL",
            arenaName = "Amway Center",
            arenaLocation = LatLng(28.539167, -81.383611),
            divisionId = "southeast",
            logoEmoji = "✨",
            founded = 1989
        ),
        NBATeam(
            id = "wizards",
            name = "Washington Wizards",
            city = "Washington, DC",
            arenaName = "Capital One Arena",
            arenaLocation = LatLng(38.898056, -77.020833),
            divisionId = "southeast",
            logoEmoji = "🧙",
            founded = 1961
        )
    )
    
    // Northwest Division
    private val northwestTeams = listOf(
        NBATeam(
            id = "nuggets",
            name = "Denver Nuggets",
            city = "Denver, CO",
            arenaName = "Ball Arena",
            arenaLocation = LatLng(39.748611, -105.007500),
            divisionId = "northwest",
            logoEmoji = "⛰️",
            founded = 1967
        ),
        NBATeam(
            id = "timberwolves",
            name = "Minnesota Timberwolves",
            city = "Minneapolis, MN",
            arenaName = "Target Center",
            arenaLocation = LatLng(44.979444, -93.276111),
            divisionId = "northwest",
            logoEmoji = "🐺",
            founded = 1989
        ),
        NBATeam(
            id = "thunder",
            name = "Oklahoma City Thunder",
            city = "Oklahoma City, OK",
            arenaName = "Paycom Center",
            arenaLocation = LatLng(35.463333, -97.515000),
            divisionId = "northwest",
            logoEmoji = "⚡",
            founded = 1967
        ),
        NBATeam(
            id = "blazers",
            name = "Portland Trail Blazers",
            city = "Portland, OR",
            arenaName = "Moda Center",
            arenaLocation = LatLng(45.531667, -122.666667),
            divisionId = "northwest",
            logoEmoji = "🌲",
            founded = 1970
        ),
        NBATeam(
            id = "jazz",
            name = "Utah Jazz",
            city = "Salt Lake City, UT",
            arenaName = "Delta Center",
            arenaLocation = LatLng(40.768333, -111.901111),
            divisionId = "northwest",
            logoEmoji = "🎵",
            founded = 1974
        )
    )
    
    // Pacific Division
    private val pacificTeams = listOf(
        NBATeam(
            id = "warriors",
            name = "Golden State Warriors",
            city = "San Francisco, CA",
            arenaName = "Chase Center",
            arenaLocation = LatLng(37.768056, -122.387500),
            divisionId = "pacific",
            logoEmoji = "🌉",
            founded = 1946
        ),
        NBATeam(
            id = "clippers",
            name = "LA Clippers",
            city = "Los Angeles, CA",
            arenaName = "Crypto.com Arena",
            arenaLocation = LatLng(34.043056, -118.267222),
            divisionId = "pacific",
            logoEmoji = "⛵",
            founded = 1970
        ),
        NBATeam(
            id = "lakers",
            name = "Los Angeles Lakers",
            city = "Los Angeles, CA",
            arenaName = "Crypto.com Arena",
            arenaLocation = LatLng(34.043056, -118.267222),
            divisionId = "pacific",
            logoEmoji = "💜",
            founded = 1947
        ),
        NBATeam(
            id = "suns",
            name = "Phoenix Suns",
            city = "Phoenix, AZ",
            arenaName = "Footprint Center",
            arenaLocation = LatLng(33.445833, -112.071389),
            divisionId = "pacific",
            logoEmoji = "☀️",
            founded = 1968
        ),
        NBATeam(
            id = "kings",
            name = "Sacramento Kings",
            city = "Sacramento, CA",
            arenaName = "Golden 1 Center",
            arenaLocation = LatLng(38.580278, -121.499722),
            divisionId = "pacific",
            logoEmoji = "👑",
            founded = 1923
        )
    )
    
    // Southwest Division
    private val southwestTeams = listOf(
        NBATeam(
            id = "mavericks",
            name = "Dallas Mavericks",
            city = "Dallas, TX",
            arenaName = "American Airlines Center",
            arenaLocation = LatLng(32.790556, -96.810278),
            divisionId = "southwest",
            logoEmoji = "🐴",
            founded = 1980
        ),
        NBATeam(
            id = "rockets",
            name = "Houston Rockets",
            city = "Houston, TX",
            arenaName = "Toyota Center",
            arenaLocation = LatLng(29.750833, -95.362222),
            divisionId = "southwest",
            logoEmoji = "🚀",
            founded = 1967
        ),
        NBATeam(
            id = "grizzlies",
            name = "Memphis Grizzlies",
            city = "Memphis, TN",
            arenaName = "FedExForum",
            arenaLocation = LatLng(35.138333, -90.050556),
            divisionId = "southwest",
            logoEmoji = "🐻",
            founded = 1995
        ),
        NBATeam(
            id = "pelicans",
            name = "New Orleans Pelicans",
            city = "New Orleans, LA",
            arenaName = "Smoothie King Center",
            arenaLocation = LatLng(29.948889, -90.081944),
            divisionId = "southwest",
            logoEmoji = "🦜",
            founded = 2002
        ),
        NBATeam(
            id = "spurs",
            name = "San Antonio Spurs",
            city = "San Antonio, TX",
            arenaName = "Frost Bank Center",
            arenaLocation = LatLng(29.426944, -98.4375),
            divisionId = "southwest",
            logoEmoji = "⭐",
            founded = 1967
        )
    )
    
    // All teams combined
    private val allTeams = atlanticTeams + centralTeams + southeastTeams + 
                          northwestTeams + pacificTeams + southwestTeams
    
    /**
     * Get all teams for a specific division
     */
    fun getTeamsByDivision(divisionId: String): List<NBATeam> {
        return allTeams.filter { it.divisionId == divisionId }
    }
    
    /**
     * Get a specific team by ID
     */
    fun getTeamById(teamId: String): NBATeam? {
        return allTeams.find { it.id == teamId }
    }
    
    /**
     * Get all teams
     */
    fun getAllTeams(): List<NBATeam> = allTeams
}
