package com.kai.mynote.service.Impl;

import com.kai.mynote.entities.ActiveCode;
import com.kai.mynote.repository.ActiveCodeRepository;
import com.kai.mynote.service.ActiveCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ActiveCodeServiceImpl implements ActiveCodeService {

    @Autowired
    private ActiveCodeRepository activeCodeRepository;
    @Override
    public ActiveCode findCodeByCode(String code) {
        return activeCodeRepository.getCodeByCode(code);
    }

    @Override
    public void updateCode(ActiveCode activeCode) {
        activeCode.setUsed(true);
        activeCodeRepository.save(activeCode);
    }


}
