package com.kai.mynote.service.Impl;

import com.kai.mynote.entities.UserCode;
import com.kai.mynote.repository.UserCodeRepository;
import com.kai.mynote.service.UserCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCodeServiceImpl implements UserCodeService {

    @Autowired
    private UserCodeRepository activateCodeRepository;
    @Override
    public UserCode findCodeByCode(String code) {
        return activateCodeRepository.getCodeByCode(code);
    }

    @Override
    public void updateCode(UserCode userCode) {
        userCode.setUsed(true);
        activateCodeRepository.save(userCode);
    }


}
