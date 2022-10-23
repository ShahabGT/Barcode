package ir.shahabazimi.barcode.classes

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.*

class RecyclerItemModel : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var weight: String = ""
}