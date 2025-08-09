package com.example.foodfund.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class FoodFund_editText(context: Context, attrs: AttributeSet):AppCompatEditText(context, attrs) {

    init {
        //call function
        applyFont()
    }

    private fun applyFont() {
        // this gets the file from the assets folder and applies it
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
    }

}