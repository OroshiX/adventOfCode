package advent.di

import advent.service.AdventLogger
import advent.service.AdventLoggerImpl
import advent.service.AdventSolver
import advent.service.AdventSolverImpl
import advent.ui.config.ConfigManipulator
import advent.ui.config.ConfigManipulatorImpl
import advent.ui.input.AskInputPresenter
import advent.ui.input.AskInputPresenterImpl
import advent.ui.running.DayPresenter
import advent.ui.running.DayPresenterImpl
import advent.ui.running.dialog.MissingInformationPresenter
import advent.ui.running.dialog.MissingInformationPresenterImpl
import org.koin.dsl.module
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun appModules() = listOf(
    dayModule,
    uiModule,
    dataModule,
)

val uiModule = module {
    factory<AskInputPresenter> { AskInputPresenterImpl(configManipulator = get()) }
    factory<MissingInformationPresenter> { parameters ->
        MissingInformationPresenterImpl(missingInformation = parameters.get())
    }
    factory<DayPresenter> { parameters ->
        DayPresenterImpl(
            configManipulator = get(),
            dayRunning = parameters.get(),
            adventSolver = get(),
        )
    }
}

@OptIn(ExperimentalTime::class)
val dataModule = module {
    single<Clock> { Clock.System }
    single<AdventLogger> { AdventLoggerImpl() }
    single<ConfigManipulator> { ConfigManipulatorImpl() }
}

@OptIn(ExperimentalTime::class)
val dayModule = module {
    single<AdventSolver> {
        AdventSolverImpl(
            logger = get(),
            configManipulator = get(),
            clock = get(),
        )
    }
}