package com.sample.chatapp4.ModelClasses

data class Chat(var sender : String ?=null, // 보낸사람
                var message : String ?=null, // 메세지 내용
                var receiver : String?=null, // 받은 사람
                var isseen : Boolean =false, // 봤는지 확인
                var url : String?=null,  // 이미지 주소
                var messageId : String?=null)  // 메세지 아이디(메세지 구별)
//class Chat {
//    private var sender : String = ""
//    private var messasge : String = ""
//    private var receiver : String = ""
//    private var isseen : Boolean = false
//    private var url : String = ""
//    private var messageId : String = ""
//
//    constructor()
//
//    constructor(
//        sender: String,
//        messasge: String,
//        receiver: String,
//        isseen: Boolean,
//        url: String,
//        messageId: String
//    ) {
//        this.sender = sender
//        this.messasge = messasge
//        this.receiver = receiver
//        this.isseen = isseen
//        this.url = url
//        this.messageId = messageId
//    }
//
//
//    fun getSender():String?{
//        return sender
//    }
//fun setSender(sender:string?){
//    this.sender = sender
//}
//}