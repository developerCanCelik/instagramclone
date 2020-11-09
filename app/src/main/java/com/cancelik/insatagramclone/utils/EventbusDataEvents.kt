package com.cancelik.insatagramclone.utils

import com.cancelik.insatagramclone.model.Users

class EventbusDataEvents {
    //DUZELTME
    internal class KayıtBilgilerimiGönder(var telNo: String?, var email: String?, var verificationID: String?, var code: String?, var emailRegister: Boolean?)
    //verification ve code kısmı almamızın sebebi ise bunların telefon numarası ile kaydolma durumunda ihtiyacimizi görüyor
    internal class KullaniciBilgileriniGonder(var kullanici : Users?)
    internal class PaylasilacakResmiGonder(var imagePath : String? ,  var fileTypeImage : Boolean?)
}