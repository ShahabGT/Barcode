package ir.shahabazimi.barcode.classes

import java.io.Serializable

data class ExportDataModel(
    var data : List<String> = emptyList()
): Serializable