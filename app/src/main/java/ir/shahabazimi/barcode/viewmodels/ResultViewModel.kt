package ir.shahabazimi.barcode.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {

    private val _result: MutableLiveData<List<String>> =
        MutableLiveData()
    val result: LiveData<List<String>>
        get() = _result

    fun setResult(result:List<String>) {
        _result.value = result
    }

}
