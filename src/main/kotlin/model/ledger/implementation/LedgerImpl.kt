package model.ledger.implementation

import model.image.Image
import model.ledger.ImageLedger

object LedgerImpl:ImageLedger {
    private val ledger:MutableMap<String, Image> = HashMap()
    override fun getImage(name: String): Image {
        val image = ledger[name]
        requireNotNull(image){ "Image $name does not exist." }
        return image
    }
    override fun putImage(name: String, image: Image){
        ledger[name] = image
    }
}