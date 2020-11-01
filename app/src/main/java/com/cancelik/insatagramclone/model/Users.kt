package com.cancelik.insatagramclone.model

class Users {
    var email: String? = null
    var password: String? = null
    var name_surname: String? = null
    var user_name: String? = null
    var phone_number: String? = null
    var email_phone_number: String? = null
    var user_id : String? = null

    constructor() {}
    constructor(email: String?,phone_number: String?, password: String?, name_surname: String?, user_name: String? , email_phone_number: String?, user_id : String?) {
        this.email = email
        this.phone_number = phone_number
        this.password = password
        this.name_surname = name_surname
        this.user_name = user_name
        this.email_phone_number = email_phone_number
        this.user_id =  user_id
    }

}