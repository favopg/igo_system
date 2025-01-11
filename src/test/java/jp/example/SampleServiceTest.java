package jp.example;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    void testRegister() {
    	SampleService service = new SampleService();
        FormData input = new FormData();
        input.setBlackPlayer("イッシー");
        input.setWhitePlayer("木部夏生");
        input.setResult("黒勝ち");
        input.setKifu("nasi");

        service.register(input);
    	
    }

    @Test
    void testRegisterError() {
    	SampleService service = new SampleService();
        FormData input = new FormData();
        input.setBlackPlayer("aa");
        input.setWhitePlayer("bb");
        input.setResult("黒勝ち");
        input.setKifu("nasi");

        JSONObject response = service.register(input);
        assertEquals(response.optString("status"), "error");
        assertEquals(response.optString("message"), "黒番、白番のいずれかが登録されていないユーザです。ご確認ください");
        
    }
    
    @Test
    void testValidation2() {

    	SampleService service = new SampleService();
        FormData input = new FormData();

        JSONObject response =  ValidateUtil.validate("テスト");
        
        assertTrue(response.isEmpty());


    }


}
