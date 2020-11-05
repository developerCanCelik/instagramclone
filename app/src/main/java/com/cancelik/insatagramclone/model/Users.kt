package com.cancelik.insatagramclone.model

class Users {
    var email: String? = null
    var password: String? = null
    var name_surname: String? = null
    var user_name: String? = null
    var phone_number: String? = null
    var email_phone_number: String? = null
    var user_id : String? = null
    var userDetails : UserDetails? =null

    constructor() {}
    constructor(
        email: String?,
        password: String?,
        name_surname: String?,
        user_name: String?,
        phone_number: String?,
        email_phone_number: String?,
        user_id: String?,
        userDetails: UserDetails?
    ) {
        this.email = email
        this.password = password
        this.name_surname = name_surname
        this.user_name = user_name
        this.phone_number = phone_number
        this.email_phone_number = email_phone_number
        this.user_id = user_id
        this.userDetails = userDetails
    }

    override fun toString(): String {
        return "Users(email=$email, password=$password, name_surname=$name_surname, user_name=$user_name, phone_number=$phone_number, email_phone_number=$email_phone_number, user_id=$user_id, userDetails=$userDetails)"
    }


}