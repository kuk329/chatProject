package com.sample.chatapp4.ModelClasses

class Chatlist { // 채팅 목록을 나타낼

    private var id: String = ""

    constructor()

    constructor(id:String){
        this.id = id
    }

    fun getId():String?{
        return id
    }

    fun setId(id: String?){
        this.id = id!!
    }
}