package com.a.digistask.models

/**
 *
 *Created by Atef on 25/12/20
 *
 */
enum class Label {
    RSRP {
        override fun toString() = "rsrp"
    },
    RSRQ {
        override fun toString() = "rsrq"
    },
    SINR {
        override fun toString() = "sinr"
    }
}