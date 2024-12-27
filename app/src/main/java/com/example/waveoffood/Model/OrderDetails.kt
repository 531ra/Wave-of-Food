package com.example.waveoffood.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.navigation.NavType
import java.io.Serializable

class OrderDetails():Serializable{
    var userUid: String? = null
    var userName: String? = null
    var foodNames: MutableList<String>? = null
    var foodImages: MutableList<String>? = null
    var foodPrices: MutableList<String>? = null
    var foodQuantities: ArrayList<Int>? = null
    var address: String? = null
    var totalPrice: String? = null
    var phoneNumber: String? = null
    var orderAccepted: Boolean? = null
    var paymentReceived: Boolean? = null
    var itemPushKey: String? = null
    var currentTime: Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        paymentReceived = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: java.util.ArrayList<String>,
        foodItemImage: java.util.ArrayList<String>,
        foodItemPrice: java.util.ArrayList<String>,
        foodItemQuantity: java.util.ArrayList<Int>,
        address: String,
        totalamoount: String,
        phone: String,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        time: Long
    ) : this(){
        this.userUid=userId
        this.userName=name
        this.foodNames=foodItemName
        this.foodImages=foodItemImage
        this.foodPrices=foodItemPrice
        this.foodQuantities=foodItemQuantity
        this.address=address
        this.totalPrice=totalamoount
        this.phoneNumber=phone
        this.orderAccepted=b
        this.paymentReceived=b1
        this.itemPushKey=itemPushKey
        this.currentTime=time

    }


    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeValue(orderAccepted)
        parcel.writeValue(paymentReceived)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

   fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}
