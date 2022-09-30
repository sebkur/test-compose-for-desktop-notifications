package de.mobanisto.test.notifications.linux

import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.Structure.FieldOrder

@FieldOrder("domain", "code", "message") // NON-NLS
class GErrorStruct : Structure {
    @JvmField
    @Volatile
    var domain /* GQuark */ = 0

    @JvmField
    @Volatile
    var code = 0

    @JvmField
    @Volatile
    var message: String? = null

    constructor() {
        clear()
    }

    constructor(ptr: Pointer?) : super(ptr) {
        read()
    }
}
