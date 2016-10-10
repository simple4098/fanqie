/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomasky.fqxz.web;

import com.tomasky.fqxz.bo.param.baseInfo.XzBaseInfoBo;
import com.tomasky.fqxz.common.core.orm.Page;
import com.tomasky.fqxz.dao.XzBaseinfoRepo;
import com.tomasky.fqxz.model.XzBaseinfo;
import com.tomasky.fqxz.service.XzBaseInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeController.class);

    @Autowired
    private XzBaseInfoService xzBaseInfoService;

    @Autowired
    private XzBaseinfoRepo xzBaseinfoRepo;

    @RequestMapping("/")
    String home() {
        return "hello world!";
    }

    @RequestMapping("/inns")
    Page<XzBaseinfo> getAllInns() {
        XzBaseInfoBo param = new XzBaseInfoBo();
        param.setPayType(1);
        return xzBaseInfoService.getPageRecord(param);
    }

    @RequestMapping("/one")
    public XzBaseinfo getOne() {
        return xzBaseinfoRepo.findById(3611l);
    }

}
