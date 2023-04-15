package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.entity.Account
import tw.idv.louislee.accountingbook.domain.entity.AccountQueries
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventQueries
import kotlin.coroutines.CoroutineContext

interface AccountRepository {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<Account>>

    fun findById(id: Long, context: CoroutineContext = Dispatchers.Default): Flow<Account?>
}

@Single
internal class AccountRepositoryImpl(
    private val query: AccountQueries,
    private val accountingEventQuery: AccountingEventQueries
) : AccountRepository {
    override fun findAll(context: CoroutineContext): Flow<List<Account>> = query.findAll()
        .asFlow()
        .mapToList(context)

    override fun findById(id: Long, context: CoroutineContext): Flow<Account?> =
        query.findById(id = id)
            .asFlow()
            .mapToOneOrNull(context)
}
