package com.cancelik.insatagramclone.model

class UserDetails {
    var follower: String? = null
    var following : String? = null
    var post : String? = null
    var profile_picture : String? = null
    var biography : String?= null
    var web_site : String? = null
    //verileri çekerken kullandığımız constructor
    constructor()
    constructor(
        follower: String?,
        following: String?,
        post: String?,
        profile_picture: String?,
        biography: String?,
        web_site: String?
    ) {
        this.follower = follower
        this.following = following
        this.post = post
        this.profile_picture = profile_picture
        this.biography = biography
        this.web_site = web_site
    }
    //bütün alanları bir kerede yazdırmak için
    override fun toString(): String {
        return "UserDetails(follower=$follower, following=$following, post=$post, profile_picture=$profile_picture, biography=$biography, web_site=$web_site)"
    }



}