package view

import view.implementation.ViewImpl

interface View {
    companion object {
        fun getDefault(): View = ViewImpl()
    }
}