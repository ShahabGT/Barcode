package ir.shahabazimi.barcode.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Barcodes")
data class RecyclerItemModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "weight") val weight: String?,
    @ColumnInfo(name = "date") val date: Long?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        return if (other is RecyclerItemModel) {
            this.id == other.id &&
                    this.weight == other.weight &&
                    this.date == other.date
        } else false
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (weight?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }
}