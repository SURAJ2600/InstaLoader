package com.suraj.instaloaderapp.repo

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.suraj.instaloaderapp.datastore.Remote
import com.suraj.instaloaderapp.datastore.model.PinView
import com.suraj.instaloaderapp.repo.mapper.PinViewStateConverter
import com.suraj.instaloaderapp.testHelper.FakeObjectProvider
import com.suraj.instaloaderapp.viewstate.PinViewState
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONException
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class PinViewRepositoryTest {

    lateinit var repository: PinViewRepository
    var converter=mock<PinViewStateConverter>()
    var remote = mock<Remote>()
    private val resultPinViewState = PinViewState.Success(listOf(PinView.EMPTY))
    private val loadingPinViewState = PinViewState.Loading



    @Before
    fun setup(){
        repository = PinViewRepository(remote,converter)
    }


    @Test
    fun `should complete execution when getPinViewData() method get called`(){
        var pinViewData = FakeObjectProvider.makePinViewData()
        whenever(remote.getPinViewData(FakeObjectProvider.id)).thenReturn(Observable.just(listOf(pinViewData)))
        whenever(converter.apply(listOf(pinViewData))).thenReturn(resultPinViewState)
        val testObserver = repository.getPinViewData(FakeObjectProvider.id).test()
        testObserver.assertComplete()

    }


    @Test
    fun `should return PInView state  when getPinViewData() method get called`(){
        var pinViewData = FakeObjectProvider.makePinViewData()
        whenever(remote.getPinViewData(FakeObjectProvider.id)).thenReturn(Observable.just(listOf(pinViewData)))
        whenever(converter.apply(listOf(pinViewData))).thenReturn(resultPinViewState)
        val testObserver = repository.getPinViewData(FakeObjectProvider.id).test()
        testObserver.assertValues(loadingPinViewState,resultPinViewState)

    }

    @Test
    fun `should return error PinView state  when getPinViewData() method get called`(){
        whenever(remote.getPinViewData(FakeObjectProvider.id)).thenReturn(Observable.error(JSONException("json error")))
        val testObserver = repository.getPinViewData(FakeObjectProvider.id).test()
        val errorViewState = PinViewState.Error("NO DATA")
        testObserver.assertValues(loadingPinViewState,errorViewState)





    }

}