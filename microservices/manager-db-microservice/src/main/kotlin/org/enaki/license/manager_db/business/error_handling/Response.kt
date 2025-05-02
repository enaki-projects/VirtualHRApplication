package org.enaki.license.manager_db.business.error_handling

class Response<T> {
    var successfulOoperation: Boolean
    var code: Int
    var data: T?
    var error: String
    var message: String

    constructor(successful_operation: Boolean, code: Int, data: T?, error: String, message: String) {
        this.successfulOoperation = successful_operation
        this.code = code
        this.data = data
        this.error = error
        this.message = message
    }

    constructor(successful_operation: Boolean, code: Int, data: T?, error: String) {
        this.successfulOoperation = successful_operation
        this.code = code
        this.data = data
        this.error = error
        message = ""
    }

    constructor(successful_operation: Boolean, code: Int, data: T?) {
        this.successfulOoperation = successful_operation
        this.code = code
        this.data = data
        error = ""
        message = ""
    }
}
