package org.wit.carcrash.models

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.wit.carcrash.helpers.readImageFromPath
import timber.log.Timber.i
import java.io.ByteArrayOutputStream
import java.io.File

class CarCrashFireStore(val context: Context) : CarCrashStore {
    val carcrashs = ArrayList<CarCrashModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override suspend fun findAll(): List<CarCrashModel> {
        return carcrashs
    }

    override suspend fun findById(id: Long): CarCrashModel? {
        val foundCarCrash: CarCrashModel? = carcrashs.find { p -> p.id == id }
        return foundCarCrash
    }

    override suspend fun create(carcrash: CarCrashModel) {
        val key = db.child("users").child(userId).child("carcrashs").push().key
        key?.let {
            carcrash.fbId = key
            carcrashs.add(carcrash)
            db.child("users").child(userId).child("carcrashs").child(key).setValue(carcrash)
        }
    }

    override suspend fun update(carcrash: CarCrashModel) {
        var foundCarCrash: CarCrashModel? = carcrashs.find { p -> p.fbId == carcrash.fbId }
        if (foundCarCrash != null) {
            foundCarCrash.title = carcrash.title
            foundCarCrash.description = carcrash.description
            foundCarCrash.image = carcrash.image
            foundCarCrash.location = carcrash.location
        }

        db.child("users").child(userId).child("carcrash").child(carcrash.fbId).setValue(carcrash)

    }

    override suspend fun delete(carcrash: CarCrashModel) {
        db.child("users").child(userId).child("carcrashs").child(carcrash.fbId).removeValue()
        carcrashs.remove(carcrash)
    }

    override suspend fun clear() {
        carcrashs.clear()
    }

    fun fetchCarCrashs(carcrashsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(carcrashs) {
                    it.getValue<CarCrashModel>(
                        CarCrashModel::class.java
                    )
                }
                carcrashsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance("https://carcrashapp-a3ed4-default-rtdb.firebaseio.com").reference
        carcrashs.clear()
        db.child("users").child(userId).child("carcrashs")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImage(carcrash: CarCrashModel) {
        if (carcrash.image != "") {
            val fileName = File(carcrash.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, carcrash.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        carcrash.image = it.toString()
                        db.child("users").child(userId).child("carcrashs").child(carcrash.fbId).setValue(carcrash)
                    }
                }.addOnFailureListener{
                    var errorMessage = it.message
                    i("Failure: $errorMessage")
                }
            }
        }
    }
}