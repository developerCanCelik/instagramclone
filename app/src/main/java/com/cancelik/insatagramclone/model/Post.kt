package com.cancelik.insatagramclone.model

class Post {
    var user_id : String? = null
    var post_id : String? = null
    var upload_date : String? = null
    var comment : String? = null
    var photo_uri : String? = null

    constructor()
    constructor(
        user_id: String?,
        post_id: String?,
        upload_date: String?,
        comment: String?,
        photo_uri: String?
    ) {
        this.user_id = user_id
        this.post_id = post_id
        this.upload_date = upload_date
        this.comment = comment
        this.photo_uri = photo_uri
    }

    override fun toString(): String {
        return "Post(user_id=$user_id, post_id=$post_id, upload_date=$upload_date, comment=$comment, photo_uri=$photo_uri)"
    }


}