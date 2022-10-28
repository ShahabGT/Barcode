package ir.shahabazimi.barcode.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Barcodes")
data class RecyclerItemModel(
    @PrimaryKey() val id: String,
    @ColumnInfo(name = "weight") val weight: String?
) : Serializable