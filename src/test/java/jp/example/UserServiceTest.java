package jp.example;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class UserServiceTest {
	
    @Test
    @Disabled
    void testRgist() {

    	UserService service = new UserService();
    	UserForm form = new UserForm();
    	form.setName("イッシー");
    	form.setPassword("sakura");

    	JSONObject response = service.registUser(form);

        assertEquals(response.optString("status"), "success");

    }
    
    
    @Test
    void selectUserTest() {

    	UserService service = new UserService();
    	UserForm form = new UserForm();
    	form.setName("イッシー");
    	form.setPassword("sakura2");

    	JSONObject response = service.selectUser(form);

        assertEquals(response.optString("username"), "イッシー");
        assertEquals(response.optString("status"), "success");

    }

}
