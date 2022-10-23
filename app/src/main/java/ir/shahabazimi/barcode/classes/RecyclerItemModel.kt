package ir.shahabazimi.barcode.classes

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.*

class RecyclerItemModel : RealmObject {
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
    var weight: String = ""
}