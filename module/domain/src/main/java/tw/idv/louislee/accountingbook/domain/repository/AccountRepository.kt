package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.entity.Account
import tw.idv.louislee.accountingbook.domain.entity.AccountQueries
import kotlin.coroutines.CoroutineContext

interface AccountRepository {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<Account>>
}

@Single
internal class AccountRepositoryImpl(private val query: AccountQueries) : AccountRepository {
    override fun findAll(context: CoroutineContext): Flow<List<Account>> = query.findAll()
        .asFlow()
        .mapToList(context)
}
