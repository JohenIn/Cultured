package com.android.exampke.cultured

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun fetchArtwork(onResult: (Artwork?) -> Unit) {
    val db = Firebase.firestore
    db.collection("artworks").document("monalisa")
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val artwork = document.toObject(Artwork::class.java)
                onResult(artwork)
            } else {
                onResult(null)
            }
        }
        .addOnFailureListener { exception ->
            // 에러 처리
            onResult(null)
        }
}
