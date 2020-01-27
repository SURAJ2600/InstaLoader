package com.suraj.instaloaderapp.datastore

import com.nhaarman.mockitokotlin2.any
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.suraj.instaloaderapp.api.ApiInterface
import com.suraj.instaloaderapp.testHelper.FakeObjectProvider
import com.suraj.instaloaderapp.utils.AppConstants
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

@RunWith(JUnit4::class)
class RemoteTest {

    var converter= mock<ApiConverter>()
    var api= mock<ApiInterface>()

    var remote  =Remote(apiConverter = converter,apiInterface = api)

    @Before
    fun setup(){
        remote = Remote(apiConverter = converter,apiInterface = api)
    }

    @Test
    fun `should complete the method call when getPinViewData() called`(){
        whenever(api.getPinViewFromApi(FakeObjectProvider.id)).thenReturn(Observable.just(listOf(FakeObjectProvider.makeApiData())))
        whenever(converter.apply(listOf(FakeObjectProvider.makeApiData()))).thenReturn(listOf(FakeObjectProvider.makePinViewData()))
        val testobserver = remote.getPinViewData(FakeObjectProvider.id).test()
        testobserver.assertComplete()

    }

    @Test
    fun `is the api interface getting called`(){
        whenever(api.getPinViewFromApi(FakeObjectProvider.id)).thenReturn(Observable.just(listOf(FakeObjectProvider.makeApiData())))
        whenever(converter.apply(listOf(FakeObjectProvider.makeApiData()))).thenReturn(listOf(FakeObjectProvider.makePinViewData()))
         remote.getPinViewData(FakeObjectProvider.id).test()
        verify(api).getPinViewFromApi(any())

    }
    @Test
    fun `Should api use correct path parameter when getPinViewData() called from remote`(){
        whenever(api.getPinViewFromApi(FakeObjectProvider.id)).thenReturn(Observable.just(listOf(FakeObjectProvider.makeApiData())))
        whenever(converter.apply(listOf(FakeObjectProvider.makeApiData()))).thenReturn(listOf(FakeObjectProvider.makePinViewData()))
        remote.getPinViewData(FakeObjectProvider.id).test()
        verify(api).getPinViewFromApi("id")

    }
    @Test
    fun `should return list of PinView when getPinViewData() method called`(){
        var apiResponse = listOf(FakeObjectProvider.makeApiData())
        var pinViewData = listOf(FakeObjectProvider.makePinViewData())
        whenever(api.getPinViewFromApi(FakeObjectProvider.id)).thenReturn(Observable.just(apiResponse))
        whenever(converter.apply(apiResponse)).thenReturn(pinViewData)
        val testObserver = remote.getPinViewData(FakeObjectProvider.id).test()
        testObserver.assertValue(pinViewData)


    }



}