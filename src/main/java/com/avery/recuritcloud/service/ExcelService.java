package com.avery.recuritcloud.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(timeout = 30,isolation = Isolation.REPEATABLE_READ,noRollbackFor = Exception.class)//默认时30秒
@Service
public class ExcelService {



}
