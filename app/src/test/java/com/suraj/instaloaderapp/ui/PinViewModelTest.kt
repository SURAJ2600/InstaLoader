package com.suraj.instaloaderapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.suraj.instaloaderapp.repo.PinViewRepository
import com.suraj.instaloaderapp.rxHelper.AndroidDisposable
import com.suraj.instaloaderapp.rxHelper.AppRxScheduler
import com.suraj.instaloaderapp.testHelper.FakeObjectProvider
import com.suraj.instaloaderapp.ui.pinview.PinViewModel
import com.suraj.instaloaderapp.viewstate.PinViewState
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PinViewModelTest {



    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    var repository = mock<PinViewRepository>()
    var disposbale =mock<AndroidDisposable>()
    var appRxScheduler=AppRxScheduler()
    lateinit var pinViewModel:PinViewModel
    var pinViewState= PinViewState.Success(listOf(FakeObjectProvider.makePinViewData()))
    var loadingState= PinViewState.Loading
    @Before
    fun setUp(){

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
        pinViewModel= PinViewModel(repository = repository,androidDisposable = disposbale,appRxScheduler = appRxScheduler)
    }


    @Test
    fun `should  return correct data from repository`() {
        whenever(repository.getPinViewData(FakeObjectProvider.id)).thenReturn(Observable.just(pinViewState))
        pinViewModel.getPinView(FakeObjectProvider.id)
        val testobserver = pinViewModel.pinViewResponse().testObserver()
        Assert.assertNotNull(pinViewModel.pinViewResponse())
        Assert.assertEquals(testobserver.observedValues[0], pinViewState)
    }




    /*To make the live data observe forever,  and also to return the data */
    class TestObserver<T> : Observer<T> {

        val observedValues = mutableListOf<T?>()

        override fun onChanged(value: T?) {
            observedValues.add(value)
        }
    }

    fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
        observeForever(it)
    }

}