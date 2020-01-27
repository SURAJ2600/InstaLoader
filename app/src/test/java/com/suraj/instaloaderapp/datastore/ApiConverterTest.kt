package com.suraj.instaloaderapp.datastore

import com.suraj.instaloaderapp.api.model.ApiResponse
import com.suraj.instaloaderapp.datastore.model.PinView
import com.suraj.instaloaderapp.testHelper.FakeObjectProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class ApiConverterTest  {

    var converter= ApiConverter()


    @Test
    fun `should convert api data correctly into PinView`(){

        var apiResponse = listOf(FakeObjectProvider.makeApiData())
        var data = converter.apply(apiResponse)
        assertValues(data,apiResponse)

    }

    private fun assertValues(data: List<PinView>, apiResponse: List<ApiResponse>) {
        Assert.assertEquals(data.size,apiResponse.size)
        Assert.assertEquals(data.get(0).name,apiResponse.get(0).user.name)
        Assert.assertEquals(data.get(0).likes,apiResponse.get(0).likes)
        Assert.assertEquals(data.get(0).profileImage,apiResponse.get(0).user.profile_image.large)

    }

}