package domain.model

import domain.image.Image
import domain.model.implementation.LedgerImpl
import kotlinx.collections.immutable.persistentHashMapOf

interface Model {
    fun getImage(name:String): Image?
    fun putImage(name:String, image:Image): Model
    fun asMap(): Map<String, Image>
    companion object{
        fun get():Model = LedgerImpl(persistentHashMapOf())
    }
}