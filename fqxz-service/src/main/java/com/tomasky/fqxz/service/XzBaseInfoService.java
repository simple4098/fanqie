package com.tomasky.fqxz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tomasky.cache.api.Cache;
import com.tomasky.comment.rpc.CommentApi;
import com.tomasky.comment.rpc.bean.InnCommentBaseInfoBo;
import com.tomasky.fqxz.bo.param.baseInfo.XzBaseInfoBo;
import com.tomasky.fqxz.mapper.XzBaseInfoMapper;
import com.tomasky.fqxz.model.XzBaseinfo;
import com.tomasky.fqxz.service.handler.ReturnHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class XzBaseInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XzBaseInfoService.class);

    @Autowired
    private XzBaseInfoMapper xzBaseInfoMapper;

    @Autowired
    private CommentApi commentApi;

    @Autowired
    private Cache cache;

    public List<XzBaseinfo> findList(XzBaseInfoBo param) {
        return xzBaseInfoMapper.getXzBaseInfos(param);
    }

    public PageInfo<XzBaseinfo> getPageRecord(XzBaseInfoBo param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<XzBaseinfo> users = xzBaseInfoMapper.getXzBaseInfos(param);
        return new PageInfo(users);
    }


    public Map getInnPayType(Long innId) {
        XzBaseinfo xzBaseinfo = xzBaseInfoMapper.getInnPayTypeAndKnowsAndAccount(innId);
        return ReturnHandler.success(xzBaseinfo);
    }

    public InnCommentBaseInfoBo getCommentsByInnid(Integer innId) {
        LOGGER.info("---------------------getCommentsByInnid------------------------------");
        LOGGER.info(String.valueOf(cache.put("test-fqxz", "momo", 10000)));
        LOGGER.info(String.valueOf(cache.get("test-fqxz")));
        InnCommentBaseInfoBo result = commentApi.getCommentsByInnId(innId);
        return result;
    }

    public InnCommentBaseInfoBo getAssignCommentsByInnId(Integer innId, String commentIds, String impressionIds) {
        LOGGER.info("---------------------getAssignCommentsByInnId------------------------------");
        List cs;
        List is;
        if (StringUtils.isNotBlank(commentIds)) {
            cs = Arrays.asList(Arrays.stream(commentIds.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray());
        } else {
            return null;
        }
        if (StringUtils.isNotBlank(impressionIds)) {
            is = Arrays.asList(Arrays.stream(impressionIds.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray());
        } else {
            return null;
        }
        InnCommentBaseInfoBo result = commentApi.getAssignComments(innId, cs, is);
        return result;
    }

}
