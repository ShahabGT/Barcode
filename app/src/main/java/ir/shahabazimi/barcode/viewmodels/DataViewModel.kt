package ir.shahabazimi.barcode.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.shahabazimi.barcode.classes.AppDatabase
import ir.shahabazimi.barcode.classes.BarcodeDAO
import ir.shahabazimi.barcode.classes.RecyclerItemModel

class DataViewModel(
    private val db:BarcodeDAO
):ViewModel() {

    private val _result: MutableLiveData<List<RecyclerItemModel>> =
        MutableLiveData()
    val result: LiveData<List<RecyclerItemModel>>
        get() = _result

}