package com.example.part2_chapter7

import com.google.gson.annotations.SerializedName

enum class Category {

    @SerializedName("POP")
    POP,
    @SerializedName("PTY")
    PTY,
    @SerializedName("SKY")
    SKY,
    @SerializedName("TMP")
    TMP,
}