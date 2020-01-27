package com.suraj.instaloaderapp.repo

import com.suraj.instaloaderapp.datastore.model.PinView
import com.suraj.instaloaderapp.repo.mapper.PinViewStateConverter
import com.suraj.instaloaderapp.testHelper.FakeObjectProvider
import com.suraj.instaloaderapp.viewstate.PinViewState
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class PinViewStateConverterTest {

   lateinit var converterTest:PinViewStateConverter

    @Before()
    fun setUp(){
        converterTest= PinViewStateConverter()
    }

    @Test
    fun `should convert PinView  correctly into PinViewState `(){
        var pinView= listOf(FakeObjectProvider.makePinViewData())
        var data = converterTest.apply(pinView)


        assertDatas(pinView,data)


    }

    private fun assertDatas(pinview: List<PinView>, pinviewState: PinViewState) {
        Assert.assertEquals((pinviewState as PinViewState.Success).list.get(0).id,pinview.get(0).id)
        Assert.assertEquals((pinviewState as PinViewState.Success).list.get(0).name,pinview.get(0).name)
        Assert.assertEquals((pinviewState as PinViewState.Success).list.get(0).likes,pinview.get(0).likes)
        Assert.assertEquals((pinviewState as PinViewState.Success).list.get(0).profileImage,pinview.get(0).profileImage)

    }

}