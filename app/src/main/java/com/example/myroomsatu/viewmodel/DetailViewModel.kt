package com.example.myroomsatu.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myroomsatu.repositori.RepositoriSiswa
import com.example.myroomsatu.view.route.DestinasiDetailSiswa
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriSiswa: RepositoriSiswa
) : ViewModel() {

    private val siswaId: Int = checkNotNull(savedStateHandle[DestinasiDetailSiswa.itemIdArg])

    // Menggunakan nama 'uiState' agar sesuai dengan panggilan di UI
    val uiState: StateFlow<DetailUiState> =
        repositoriSiswa.getSiswaStream(siswaId)
            .filterNotNull()
            .map {
                DetailUiState(detailSiswa = it.toDetailSiswa())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailUiState()
            )

    // Menggunakan viewModelScope.launch agar UI tinggal panggil tanpa perlu coroutine scope
    fun deleteItem() {
        viewModelScope.launch {
            repositoriSiswa.deleteSiswa(uiState.value.detailSiswa.toSiswa())
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

// Menggunakan nama 'DetailUiState' agar sesuai dengan UI
data class DetailUiState(
    val detailSiswa: DetailSiswa = DetailSiswa()
)