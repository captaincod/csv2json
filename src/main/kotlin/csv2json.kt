import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.gson.Gson
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val file = File("src/main/resources/googleplaystore.csv")
    var rows: List<List<String>> = csvReader().readAll(file)
    rows = rows.subList(1,rows.size)

    val categories = mutableSetOf<String>()

    // перевод названий категорий
    val categoriesRussian = mapOf(
        "ART_AND_DESIGN" to "ИСКУССТВО И ДИЗАЙН", "AUTO_AND_VEHICLES" to "АВТОМОБИЛИ И ТРАНСПОРТ", "BEAUTY" to "КРАСОТА",
        "BOOKS_AND_REFERENCE" to "КНИГИ", "BUSINESS" to "БИЗНЕС", "COMICS" to "КОМИКСЫ", "COMMUNICATION" to "ОБЩЕНИЕ",
        "DATING" to "ЗНАКОМСТВА", "EDUCATION" to "ОБРАЗОВАНИЕ", "ENTERTAINMENT" to "РАЗВЛЕЧЕНИЯ", "EVENTS" to "СОБЫТИЯ",
        "FINANCE" to "ФИНАНСЫ", "FOOD_AND_DRINK" to "ЕДА И НАПИТКИ", "HEALTH_AND_FITNESS" to "ЗДОРОВЬЕ И ФИТНЕС",
        "HOUSE_AND_HOME" to "ДОМ", "LIBRARIES_AND_DEMO" to "БИБЛИОТЕКИ", "LIFESTYLE" to "ОБРАЗ ЖИЗНИ", "GAME" to "ИГРЫ",
        "FAMILY" to "СЕМЬЯ", "MEDICAL" to "МЕДИЦИНА", "SOCIAL" to "СОЦИАЛЬНЫЕ", "SHOPPING" to "ШОППИНГ",
        "PHOTOGRAPHY" to "ФОТОГРАФИЯ", "SPORTS" to "СПОРТ", "TRAVEL_AND_LOCAL" to "ПУТЕШЕСТВИЯ", "TOOLS" to "ИНСТРУМЕНТЫ",
        "PERSONALIZATION" to "ПЕРСОНАЛИЗАЦИЯ", "PRODUCTIVITY" to "ПРОДУКТИВНОСТЬ", "PARENTING" to "ВОСПИТАНИЕ ДЕТЕЙ",
        "WEATHER" to "ПОГОДА", "VIDEO_PLAYERS" to "ВИДЕОПЛЕЕРЫ", "NEWS_AND_MAGAZINES" to "НОВОСТИ И ЖУРНАЛЫ",
        "MAPS_AND_NAVIGATION" to "КАРТЫ И НАВИГАЦИЯ"
    )

    for (row in rows){
        categories += categoriesRussian[row[1]].toString()
    }
    val map = mutableMapOf<String,ArrayList<Map<String,String>>>()

    for (c in categories){
        map[c] = arrayListOf(mapOf())
    }

    // сам парсинг
    for (row in rows){
        val appName = row[0]
        val rating = row[2]
        val reviews = row[3]
        val size = row[4]
        val installs: String =
            if (row[5].indexOf(",") > 0){
                if (row[5].indexOf("+") > 0){
                    row[5].split(",").joinToString(separator = "").substring(0,row[5].split(",").joinToString(separator = "").indexOf("+")).toInt().toString()
                } else row[5].split(",").joinToString(separator = "")
            } else {
                if (row[5].indexOf("+") > 0){
                    row[5].substring(0,row[5].indexOf("+")).toInt().toString()
                } else row[5]
            }

        val isFree: String = if (row[6] == "Free"){
            "true"
        } else "false"
        val contentRating = row[8]
        val genres: String = if (row[9].indexOf(";") > 0){
            row[9].split(";").joinToString(separator = ", ")
        } else row[9]

        var lastUpdated: String
        val tz = TimeZone.getTimeZone("UTC")
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
        df.timeZone = tz
        if (row[10].split(" ").size > 1){
            val date = row[10].split(" ")
            var month = 1
            when (date[0]){
                "January" -> month = 1
                "February" -> month = 2
                "March" -> month = 3
                "April" -> month = 4
                "May" -> month = 5
                "June" -> month = 6
                "July" -> month = 7
                "August" -> month = 8
                "September" -> month = 9
                "October" -> month = 10
                "November" -> month = 11
                "December" -> month = 12
            }
            //lastUpdated = df.format(LocalDate.of(date[2].toInt(),month,date[1].substring(0,date[1].indexOf(",")).toInt())).toString()
            lastUpdated = LocalDate.of(date[2].toInt(),month,date[1].substring(0,date[1].indexOf(",")).toInt()).toString()
        } else lastUpdated = LocalDate.now().toString()

        val currentVersion = row[11]
        var androidVer: String
        when(row[12].substring(0,3)){
            "1.0" -> androidVer = "1"
            "1.1" -> androidVer = "2"
            "1.5" -> androidVer = "3"
            "1.6" -> androidVer = "4"
            "2.0" -> androidVer = "5"
            "2.2" -> androidVer = "8"
            "2.3" -> androidVer = "9"
            "3.0" -> androidVer = "11"
            "4.0" -> androidVer = "14"
            "4.1" -> androidVer = "16"
            "4.2" -> androidVer = "17"
            "4.3" -> androidVer = "18"
            "4.4" -> androidVer = "19"
            "5.0" -> androidVer = "21"
            "5.1" -> androidVer = "22"
            "6.0" -> androidVer = "23"
            "7.0" -> androidVer = "24"
            "7.1" -> androidVer = "25"
            "8.0" -> androidVer = "26"
            "8.1" -> androidVer = "27"
            "9.0" -> androidVer = "28"
            "10.0" -> androidVer = "29"
            else -> androidVer = row[12].substring(0,3)
        }

        val app = mapOf("appName" to appName, "rating" to rating, "reviews" to reviews, "size" to size, "installs" to installs,
            "isFree" to isFree, "contentRating" to contentRating, "genres" to genres, "lastUpdated" to lastUpdated,
            "currentVersion" to currentVersion, "androidVer" to androidVer)

        map[categoriesRussian[row[1]].toString()]?.add(app)
    }
    for(key in map.keys){
        map[key]?.removeAt(0)
    }

    // вывод в файл .txt
    var out = ""
    for(key in map.keys){
        out += "$key: ${map[key]} \n"
        out += "\n"
    }
    File("src/main/resources/out.txt").writeText(out)

    // вывод как json
    val gson = Gson()
    val json = gson.toJson(map)
    File("src/main/resources/out.json").writeText(json)

}