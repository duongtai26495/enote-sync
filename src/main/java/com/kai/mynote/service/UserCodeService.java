package com.kai.mynote.service;

import com.kai.mynote.entities.UserCode;

public interface UserCodeService {

    UserCode findCodeByCode(String code);

    void updateCode(UserCode userCode);

}
