package com.sample.chatapp4.ModelClasses

data class Chat(var sender : String ?=null,
                var message : String ?=null,
                var receiver : String?=null,
                var isseen : Boolean =false,
                var url : String?=null,
                var messageId : String?=null)
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