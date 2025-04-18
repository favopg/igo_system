package jp.favoriteigo;

import static org.junit.jupiter.api.Assertions.*;

import jp.favoriteigo.form.UserForm;
import jp.favoriteigo.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class UserServiceTest {
	
    @Test
    @Disabled
    void registerUserTest() {

    	UserService service = new UserService();
    	UserForm form = new UserForm();
    	form.setName("ロボット");
    	form.setPassword("123");
        form.setPublic(true);
        form.setChessAbility("六段");
        form.setChessAbilityKbn("0");

    	JSONObject response = service.registerUser(form);

        assertEquals("success", response.optString("status"));

    }
    
    
    @Test
    void selectUserTest() {

    	UserService service = new UserService();
    	UserForm form = new UserForm();
    	form.setName("ロボット");
    	form.setPassword("123");

    	JSONObject response = service.selectUser(form);

        assertEquals("ロボット", response.optString("name"));
        assertEquals("success", response.optString("status"));

    }

}
