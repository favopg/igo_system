package jp.example;

import java.util.Map;

import org.junit.jupiter.api.Test;

class SampleDaoTest {
	
    @Test
    void testGetNameId() {
    	Map<String, Object> data = Map.of(
    			"blackPlayer", "イッシー",
    			"whitePlayer", "木部夏生",
    			"InblackPlayer", "イッシー",
    			"InwhitePlayer", "木部夏生"

    			);
    	System.out.println("検索後"+ SampleDao.selectRosterId("sql/rosters_select_name.sql", data).toString(2));    	
    }
}
