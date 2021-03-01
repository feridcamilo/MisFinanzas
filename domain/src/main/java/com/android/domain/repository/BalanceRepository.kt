package com.android.domain.repository

import com.android.domain.model.Balance

interface BalanceRepository {
    suspend fun getBalance(query: String): Balance
    suspend fun getCloudBalance(clientId: String): Balance
}