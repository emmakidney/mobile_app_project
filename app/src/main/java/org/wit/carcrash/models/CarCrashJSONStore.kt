package org.wit.carcrash.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.carcrash.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "carcrashs.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<CarCrashModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CarCrashJSONStore(private val context: Context) : CarCrashStore {

    var carcrashs = mutableListOf<CarCrashModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override suspend fun findAll(): MutableList<CarCrashModel> {
        logAll()
        return carcrashs
    }

    override suspend fun create(carcrash: CarCrashModel) {
        carcrash.id = generateRandomId()
        carcrashs.add(carcrash)
        serialize()
    }


    override suspend fun update(carcrash: CarCrashModel) {
        val carcrashsList = findAll() as ArrayList<CarCrashModel>
        var foundCarCrash: CarCrashModel? = carcrashsList.find { p -> p.id == carcrash.id }
        if (foundCarCrash != null) {
            foundCarCrash.title = carcrash.title
            foundCarCrash.description = carcrash.description
            foundCarCrash.image = carcrash.image
            foundCarCrash.lat = carcrash.lat
            foundCarCrash.lng = carcrash.lng
            foundCarCrash.zoom = carcrash.zoom
        }
        serialize()
    }

    override suspend fun delete(carcrash: CarCrashModel) {
       val foundCarCrash: CarCrashModel? = carcrashs.find {it.id == carcrash.id}
        carcrashs.remove(foundCarCrash)
        serialize()
    }

    override suspend fun findById(id: Long): CarCrashModel? {
        val foundCarCrash: CarCrashModel? = carcrashs.find {it.id == id}
        return foundCarCrash
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(carcrashs, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        carcrashs = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        carcrashs.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}

