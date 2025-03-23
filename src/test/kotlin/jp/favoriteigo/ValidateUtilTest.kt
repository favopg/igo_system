package jp.favoriteigo

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class ValidateUtilTest {
    @Test
    fun validateTest() {
       val testData = MatchForm()
       val result : JSONObject = ValidateUtil.validate(testData)
        assertEquals(result.optString("status"), "error")
    }
 }