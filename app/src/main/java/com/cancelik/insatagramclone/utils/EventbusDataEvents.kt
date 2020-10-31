package com.cancelik.insatagramclone.utils

class EventbusDataEvents {
    //DUZELTME
    internal class KayıtBilgilerimiGönder(var telNo: String?, var email: String?, var verificationID: String?, var code: String?, var emailRegister: Boolean?)
    //verification ve code kısmı almamızın sebebi ise bunların telefon numarası ile kaydolma durumunda ihtiyacimizi görüyor
}