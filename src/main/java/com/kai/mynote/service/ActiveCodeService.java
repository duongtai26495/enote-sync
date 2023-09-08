package com.kai.mynote.service;

import com.kai.mynote.entities.ActiveCode;

public interface ActiveCodeService {

    ActiveCode findCodeByCode(String code);
}
