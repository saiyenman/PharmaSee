package com.example.pharma.Services

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.pharma.AdapterItems.PharmacieItem
import com.example.pharma.AllFragment.Companion.pharmListProgressBar
import com.example.pharma.AllFragment.Companion.pharmaciesAdapter
import com.example.pharma.Model.Pharmacie
import com.example.pharma.Model.Ville
import com.example.pharma.PharmacieModalActivity
import com.example.pharma.Utils.ADD_PHARM_TO_DATABASE
import com.example.pharma.Utils.PHARMACIE_PARCEL
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.absoluteValue

object DataService {

    val pharmacies = fillPharmacies()
    val villeList : ArrayList<Ville> = arrayListOf(Ville( 1, "Adrar"),
        Ville(  2, "Chlef"),
        Ville(  3, "Laghouat"),
        Ville(  4, "OEB"),
        Ville(  5, "Batna"),
        Ville(  6, "Béjaïa"),
        Ville(  7, "Biskra"),
        Ville(  8, "Béchar"),
        Ville(  9, "Blida"),
        Ville(  10, "Bouira"),
        Ville(  11, "Tamanrasset"),
        Ville(  12, "Tébessa"),
        Ville(  13, "Tlemcen"),
        Ville(  14, "Tiaret"),
        Ville(  15, "Tizi Ouzou"),
        Ville(  16, "Alger"),
        Ville(  17, "Djelfa"),
        Ville(  18, "Jijel"),
        Ville(  19, "Sétif"),
        Ville(  20, "Saïda"),
        Ville(  21, "Skikda"),
        Ville(  22, "SBA"),
        Ville(  23, "Annaba"),
        Ville(  24, "Guelma"),
        Ville(  25, "Constantine"),
        Ville(  26, "Médéa"),
        Ville(  27, "Mostaganem"),
        Ville(  28, "M'Sila"),
        Ville(  29, "Mascara"),
        Ville(  30, "Ouargla"),
        Ville(  31, "Oran"),
        Ville(  32, "El Bayadh"),
        Ville(  33, "Illizi"),
        Ville(  34, "BBA"),
        Ville(  35, "Boumerdès"),
        Ville(  36, "El Tarf"),
        Ville(  37, "Tindouf"),
        Ville(  38, "Tissemsilt"),
        Ville(  39, "El Oued"),
        Ville(  40, "Khenchela"),
        Ville(  41, "Souk Ahras"),
        Ville(  42, "Tipaza"),
        Ville(  43, "Mila"),
        Ville(  44, "Aïn Defla"),
        Ville(  45, "Naâma"),
        Ville(  46, "Aïn Tém"),
        Ville(  47, "Ghardaïa"),
        Ville(  48, "Relizane") )

    fun fillPharmacies(): ArrayList<Pharmacie> {
        /*val p1 = Pharmacie("20 rue rezine med", 1,  "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p2 = Pharmacie("20 rue rezine med", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p3 = Pharmacie("20 rue rezine med", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p4 = Pharmacie("20 rue rezine med", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p5 = Pharmacie("20 rue rezine med", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p6 = Pharmacie("20 rue rezine med", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p7 = Pharmacie("20 rue rezine med", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p8 = Pharmacie("20 rue rezine med", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p9 = Pharmacie("20 rue rezine med", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p10 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p11 = Pharmacie("20 rue rezine med", 10, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.8992862, 36.3736879)
        val p12 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p13 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p14 = Pharmacie("20 rue rezine med", 10, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.8992862, 36.3736028)
        val p15 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p16 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p17 = Pharmacie("20 rue rezine med", 10, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.8992862, 36.3736028)
        val p18 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p19 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p20 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p21 = Pharmacie("20 rue rezine med", 10, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.8993885, 36.3736328)
        val p22 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p23 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p24 = Pharmacie("20 rue rezine med", 10, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.8968936, 36.3745747)
        val p25 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p26 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p27 = Pharmacie("20 rue rezine med", 10, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.894611, 36.3754216)
        val p28 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p29 = Pharmacie("20 rue rezine med", 12, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)
        val p30 = Pharmacie("20 rue rezine med", 10, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 12.00, 32.09)*/
        val p1 = Pharmacie("Rue Hadj Ahmed Mohamed, Hydra 16000", 16,  "08:00-21:00", "0558857774", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.032691, 36.748287 )
        val p2 = Pharmacie("Rue Mohamed Belouizdad, Sidi M'Hamed", 16, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.053463, 36.753851 )
        val p3 = Pharmacie("7115 Rue Didouche Mourad, Alger Ctre 16000", 16, "12:00-14:00", "021646990", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.0438185, 36.763488 )

        val p4 = Pharmacie("80 LOGEMENTS , Adrar، Adrar", 1,  "08:00-21:00", "049960635", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 27.977312, -0.344045)
        val p5 = Pharmacie("Rue Mohamed, Adrar", 1, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 27.977312, -0.344045)
        val p6 = Pharmacie("Unnamed Road, Adrar, Adrar", 1, "12:00-14:00", "049 35 15 64", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 27.977312, -0.344045)

        val p7 = Pharmacie("Place de la solidarité, Chlef", 2,  "08:00-21:00", "027 77 01 01", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 36.169479, 1.283860)
        val p8 = Pharmacie("zone 09 chettia، Chettia", 2, "00:00-23:59", "0555 58 32 48", arrayListOf("CNAS", "BADR"), "https://www.facebook.com/PharmacieLakoues", 36.210634, 1.253631)
        val p9 = Pharmacie("Rue d'el-karimia, Oued Fodda", 2, "12:00-14:00", "027 44 87 44", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 36.192455, 1.525670)

        val p10 = Pharmacie("حي الوئام، Laghouat 03000", 3,  "08:00-21:00", "0560 88 93 51", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 33.791781, 2.845145)
        val p11 = Pharmacie("1er novembre laghouat، Laghouat", 3, "12:00-14:00", "029 10 33 39", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 33.824309, 2.869188)
        val p12 = Pharmacie("Rue Masdej Elhoucine Elmaamouraa, Laghouat 03000", 3, "12:00-14:00", "029 10 07 00", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 33.800198, 2.866091)

        val p13 = Pharmacie("Zerdani Hessouna, Oum el Bouaghi", 4,  "08:00-21:00", "0671 35 17 21", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 35.868701, 7.121886)
        val p14 = Pharmacie("Boulevard Houari Boumediene, Oum el Bouaghi", 4, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 35.868563, 7.114419)
        val p24 = Pharmacie("Aret Bus, Oum el Bouaghi", 4, "12:00-14:00", "021646990", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 35.870998, 7.109356)

        val p15 = Pharmacie("24 Avenue de la République, Batna", 5,  "08:00-21:00", "0558857774", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 35.556259, 6.176953)
        val p16 = Pharmacie("Rue Hadj Abdelmadjid Abbdessamed, Batna", 5, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 35.553116, 6.174634)
        val p17 = Pharmacie("Rue de la Mosquée, Batna", 5, "12:00-14:00", "021646990", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 35.544592, 6.194804)

        val p18 = Pharmacie("Boulevard de l'A.L.N, Béjaïa", 6,  "08:00-21:00", "0558857774", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 36.751654, 5.062388)
        val p19 = Pharmacie("Rue Mahfoudi Fatah, Béjaïa", 6, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 36.746978, 5.052859)
        val p20 = Pharmacie("N12 Boulevard Krim Belkacem, Béjaïa 06000", 6, "12:00-14:00", "021646990", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 36.732671, 5.051742)

        val p21 = Pharmacie("Rue Manssouri Belkacem, Biskra 07000", 7,  "08:00-21:00", "0558857774", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 34.853077, 5.740771)
        val p22 = Pharmacie("Boulevard Zaatcha, Biskra", 7, "12:00-14:00", "0946789574", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 34.848222, 5.718279)
        val p23 = Pharmacie("Cité 46 logement, Biskra", 7, "12:00-14:00", "021646990", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 34.843574, 5.685148)
        val p25 = Pharmacie("Oued Smar", 16, "12:00-16:00", "021435343", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.1785896, 36.7136053 )
        val p26 = Pharmacie("Oued Smar", 16, "12:00-16:00", "021435343", arrayListOf("CNAS", "BADR"), "facebook.com/pharm1", 3.1810784, 36.714018 )
        return arrayListOf(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23 ,p24, p25, p26)
    }

    fun fillPharmacieDatabase() {
        for (pharmacie in pharmacies) {
            val pharmKey = FirebaseDatabase.getInstance().getReference("/pharmacies").push().key
            val pharmRef = FirebaseDatabase.getInstance().getReference("/pharmacies/$pharmKey")

            pharmRef.setValue(pharmacie)
                .addOnSuccessListener {
                    Log.d(ADD_PHARM_TO_DATABASE, "Succefully added new medecin")
                }
                .addOnFailureListener {
                    Log.d(ADD_PHARM_TO_DATABASE, "Failed added new medecin because ${it.message}")
                }
        }
    }

    fun fetchPharmaciesByVilleId(id: Int) {
        val pharmacieRef = FirebaseDatabase.getInstance().getReference("/pharmacies")
        pharmacieRef.keepSynced(true)  // To synch the list of pharmacies for offline
        pharmacieRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                pharmaciesAdapter.clear()
                pharmListProgressBar.visibility = View.VISIBLE
                p0.children.forEach {
                    val pharmacie = it.getValue(Pharmacie::class.java)
                    if (pharmacie != null) {
                        if (pharmacie.ville == id) {
                            pharmaciesAdapter.add(PharmacieItem(pharmacie))
                        }
                    }
                }
                pharmListProgressBar.visibility = View.GONE
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, "Cannot load pharmacie list")
            }
        })
    }

    fun fetchClosePharmacies(latitude: Double, longitude: Double, mMap: GoogleMap, context: Context) {
        val pharmacieRef = FirebaseDatabase.getInstance().getReference("/pharmacies")
        val detailsIntent = Intent(context, PharmacieModalActivity::class.java)
        pharmacieRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val pharmacie = it.getValue(Pharmacie::class.java)
                    if (((pharmacie!!.latitude - latitude).absoluteValue < 1.00) && ((pharmacie!!.longitude -longitude).absoluteValue < 1.00)) {
                       /*if (pharmacie!!.ville == 16) {*/

                        mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(pharmacie.latitude, pharmacie.longitude))
                                .title(pharmacie.telephone)
                                .snippet(pharmacie.adresse)
                        )
                        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                            override fun onMarkerClick(p0: Marker?): Boolean {
                                Log.d("Info", pharmacie!!.adresse)
                                detailsIntent.putExtra(PHARMACIE_PARCEL, pharmacie)

                                context.startActivity(detailsIntent)
                                return true
                            }
                        })
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, "Cannot load pharmacie list")
            }
        })
    }
}