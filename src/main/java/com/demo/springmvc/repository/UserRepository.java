package com.demo.springmvc.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
	@Select("SELECT corp_name FROM user_info WHERE corp_id = #{corpId} ORDER BY usr_id DESC LIMIT 1")
	String getUserStatusByCorpId(@Param("corpId") String corpId);
}
