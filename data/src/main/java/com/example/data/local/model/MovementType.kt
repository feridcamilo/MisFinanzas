package com.example.data.local.model

enum class MovementType(val type: Int) {
    CASH_INCOME(1),
    CASH_OUT(2),
    CARD_INCOME(3),
    CARD_OUT(4),
    WITHDRAWAL(5),
    CREDIT_CARD_BUY(6)
}