package jp.example

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.mockito.internal.matchers.Matches
import kotlin.test.Test

class ValidateUtilTest {
    @Test
    fun validateTest() {
       val testData = MatchForm()
       val result : JSONObject = ValidateUtil.validate(testData)
        assertEquals(result.optString("status"), "error")
    }
 }