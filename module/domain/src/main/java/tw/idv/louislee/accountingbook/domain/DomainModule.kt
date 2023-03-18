package tw.idv.louislee.accountingbook.domain

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class DomainModule {
    @Single
    fun createDomain() = Domain("Hello, koin!")
}