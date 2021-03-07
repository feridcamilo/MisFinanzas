package com.android.domain.repository

import com.android.domain.model.Balance

interface BalanceRepository {
    suspend fun getBalance(): Balance
    suspend fun getCloudBalance(clientId: String): Balance
}
