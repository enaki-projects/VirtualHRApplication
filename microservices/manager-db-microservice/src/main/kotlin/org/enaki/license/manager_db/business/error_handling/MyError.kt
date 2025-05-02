package org.enaki.license.manager_db.business.error_handling

import java.text.SimpleDateFormat
import java.util.*

class MyError(var status: Int, var error: String, var message: String, var path: String) {
    var timestamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
}
