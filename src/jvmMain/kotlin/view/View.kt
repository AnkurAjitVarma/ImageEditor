package view

import view.implementation.ViewImpl

interface View {
    fun launch(state: State, handler: Handler)
    companion object {
        fun getDefault(): View = ViewImpl()
    }
}