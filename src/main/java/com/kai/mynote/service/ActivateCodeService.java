package com.kai.mynote.service;

import com.kai.mynote.entities.ActivateCode;

public interface ActivateCodeService {

    ActivateCode findCodeByCode(String code);

    void updateCode(ActivateCode activateCode);

}
