package tw.idv.louislee.accountingbook

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.DomainModule

@Module([DomainModule::class])
class AppModule {
    // 最少要有一個Single之類的元件才會自動產生Module Class
    @Single
    fun applicationName() = "AccountingBook"
}