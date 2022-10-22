package ir.shahabazimi.barcode.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {

    private val _result: MutableLiveData<String?> =
        MutableLiveData()
    val result: LiveData<String?>
        get() = _result

    fun setResult(result:String) {
        _result.value = result
    }

    fun clear() {
        _result.value = null
    }

}
