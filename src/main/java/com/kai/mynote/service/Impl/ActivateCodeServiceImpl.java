package com.kai.mynote.service.Impl;

import com.kai.mynote.entities.ActivateCode;
import com.kai.mynote.repository.ActiveCodeRepository;
import com.kai.mynote.service.ActivateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivateCodeServiceImpl implements ActivateCodeService {

    @Autowired
    private ActiveCodeRepository activateCodeRepository;
    @Override
    public ActivateCode findCodeByCode(String code) {
        return activateCodeRepository.getCodeByCode(code);
    }

    @Override
    public void updateCode(ActivateCode activateCode) {
        activateCode.setUsed(true);
        activateCodeRepository.save(activateCode);
    }


}
