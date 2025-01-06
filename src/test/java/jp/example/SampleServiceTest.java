package jp.example;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class SampleServiceTest {
	
    @Test
    void testValidation() {

    	SampleService service = new SampleService();
        FormData input = new FormData();
        
        JSONObject validRes = service.checkValidate(input);

        assertEquals(validRes.optString("status"), "error");
        assertEquals(validRes.optString("message"), "黒番が未入力です");

    }
}
