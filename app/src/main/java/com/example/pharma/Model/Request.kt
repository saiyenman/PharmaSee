package com.example.pharma.Model

class Request(val reqId: String, val uid: String, val imageUrl: String, val descritpion: String, val status: String) {
    constructor() : this("","", "","","")
}