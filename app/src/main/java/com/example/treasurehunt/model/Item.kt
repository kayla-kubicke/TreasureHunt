//package com.example.treasurehunt.model
//
//class Item(name: String?, imageResId: Int) {
//    val name: String = getName()
//    val imageResId: Int = getImageResId()
//
//    fun getName(): String {
//        return name
//    }
//
//    fun getImageResId(): Int {
//        return imageResId
//    }
//
//}
package com.example.treasurehunt.model

data class Item(
    val name: String,
    val imageResId: Int
)
