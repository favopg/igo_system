package jp.example;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class UserServiceTest {
	
    @Test
    void testRegist() {

    	UserService service = new UserService();
    	UserForm form = new UserForm();
    	form.setName("ロボット");
    	form.setPassword("123");
        form.setPublic(true);

    	JSONObject response = service.registUser(form);

        assertEquals(response.optString("status"), "success");

    }
    
    
    @Test
    @Disabled
    void selectUserTest() {

    	UserService service = new UserService();
    	UserForm form = new UserForm();
    	form.setName("ロボット");
    	form.setPassword("123");

    	JSONObject response = service.selectUser(form);

        assertEquals(response.optString("name"), "ロボット");
        assertEquals(response.optString("status"), "success");

    }

}
