package ir.shahabazimi.barcode.classes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BarcodeDAO {
    @Query("SELECT * FROM Barcodes ORDER BY date DESC")
    fun getAll(): LiveData<List<RecyclerItemModel>>

    @Insert
    fun insertAll(vararg barcodes: RecyclerItemModel)

    @Insert
    fun insert(barcodes: RecyclerItemModel)

    @Delete
    fun delete(barcodes: RecyclerItemModel)

    @Query("DELETE FROM Barcodes")
    fun clearTable()
}