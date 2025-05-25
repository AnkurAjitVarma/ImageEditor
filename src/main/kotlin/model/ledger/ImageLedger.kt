package model.ledger

import model.image.Image
import model.ledger.implementation.LedgerImpl

interface ImageLedger {
    fun getImage(name:String):Image
    fun putImage(name:String, image:Image)
    companion object{
        fun get():ImageLedger = LedgerImpl
    }
}