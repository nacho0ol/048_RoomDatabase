package com.example.myroomsatu.repositori

import android.app.Application
import android.content.Context
import com.example.myroomsatu.room.DatabaseSiswa

interface ContainerApp {
    val repositorisiswa : RepositoriSiswa
}

class ContainerDataApp(private val context: Context):
        ContainerApp {
            override val repositorisiswa: RepositoriSiswa by lazy {
                OfflineRepositorisiswa(
                    DatabaseSiswa.getDatabase(context).siswaDao()
                )
            }
        }

class AplikasiSiswa : Application() {
    lateinit var container: ContainerApp

    override fun onCreate() {
        super.onCreate()
        container = ContainerDataApp(this)
    }
}
