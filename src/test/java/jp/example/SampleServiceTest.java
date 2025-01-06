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
        assertEquals(validRes.optString("blackPlayer"),"黒番が未入力です");

    }
    
    @Test
    void testRegister() {
    	SampleService service = new SampleService();
        FormData input = new FormData();
        input.setBlackPlayer("イッシー");
        input.setWhitePlayer("木部夏生");
        input.setResult("黒勝ち");
        input.setKifu("nasi");

        service.register(input);
    	
    }
}
