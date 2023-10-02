package com.example.moveeapp_compose_kmm.domain.usecase.accountusecase

import com.example.moveeapp_compose_kmm.data.remote.model.account.AccountDetailModel
import com.example.moveeapp_compose_kmm.data.repository.AccountRepository

class GetAccountDetailUseCase(
    private val repository: AccountRepository
) {

    suspend fun execute(): Result<AccountDetailModel> {
        return repository.getAccountDetail()
    }
}