package domain.model.implementation

import domain.image.Image
import domain.model.Model

class LedgerImpl(private val ledger: Map<String, Image>) : Model {
    override fun getImage(name: String): Image? = ledger[name]
    override fun putImage(name: String, image: Image) = LedgerImpl(ledger + (name to image))
    override fun asMap(): Map<String, Image> = ledger
}