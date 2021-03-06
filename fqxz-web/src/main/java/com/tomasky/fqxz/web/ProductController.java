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

import com.github.pagehelper.PageInfo;
import com.tomasky.fqxz.BaseController;
import com.tomasky.fqxz.bo.param.CommParam;
import com.tomasky.fqxz.bo.param.product.ProductBo;
import com.tomasky.fqxz.bo.param.product.ProductOrderBo;
import com.tomasky.fqxz.model.Product;
import com.tomasky.fqxz.service.IProductService;
import com.tomasky.fqxz.vo.ProductOrderVo;
import com.tomasky.fqxz.vo.ProductVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;


    @ApiOperation(value = "查询精品商品列表", notes = "查询精品商品列表", httpMethod = "GET")
   /* @ApiImplicitParam(name = "commParam", value = "接收参数实体CommParam", required = true, dataType = "CommParam")*/
    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    public Map getAllInns(@RequestParam Integer innId,
                          @RequestParam(required = false,defaultValue = "1") int pageNo,
                          @RequestParam(required = false,defaultValue = "10")  int pageSize) {
        try {
            CommParam commParam = new CommParam(innId,pageNo,pageSize);
            PageInfo<Product> productByInnId = productService.findProductByInnId(commParam);
            return new200(productByInnId);
        } catch (Exception e) {
            LOGGER.error("商品列表异常:", e);
            return new500(e.getMessage());
        }
    }
    @ApiOperation(value = "查询精品商品详情", notes = "查询精品商品详情", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("/detail")
    public Map<String,Object> productDetail( @ApiParam(required = true, value = "商品id") @RequestParam(name = "id", value = "id") Integer id,
                                             @ApiParam(required = true, value = "客栈id") @RequestParam(name = "innId", value = "innId") Integer innId){
        try{
            ProductBo productBo = new ProductBo();
            productBo.setId(id);
            productBo.setInnId(innId);
            ProductVo product =  productService.findProductDetail(productBo);
            return new200(product );
        }catch (Exception e){
            LOGGER.error("商品详情异常:",e);
            return new500(e.getMessage());
        }
    }

    @ApiOperation(value = "精品商品下单", notes = "精品商品下单", httpMethod = "POST")
   /* @ApiImplicitParam(name = "productOrderBo", value = "接收参数实体ProductOrderBo", required = true, dataType = "ProductOrderBo")*/
    @RequestMapping(value = "/order",method = RequestMethod.POST)
    public Map<String,Object> productOrder(@ApiParam(required = true, value = "联系人") @RequestParam(name = "contacts", value = "contacts") String contacts,
                                           @ApiParam(required = true, value = "客栈id") @RequestParam(name = "innId", value = "innId")  Integer innId ,
                                           @ApiParam(required = true, value = "商品数量") @RequestParam(name = "num", value = "num") Integer num ,
                                           @ApiParam(required = true, value = "联系电话") @RequestParam(name = "phone", value = "phone") String phone ,
                                           @ApiParam(required = true, value = "商品名称") @RequestParam(name = "proName", value = "proName") String proName,
                                           @ApiParam(required = true, value = "商品价格") @RequestParam(name = "price", value = "price") BigDecimal price ,
                                           @ApiParam(required = true, value = "商品id") @RequestParam(name = "productId", value = "productId") Long productId ,
                                           @ApiParam(required = false, value = "备注") @RequestParam(name = "remark", value = "remark") String remark ,
                                           @ApiParam(required = true, value = "总价") @RequestParam(name = "totalPrice", value = "totalPrice") BigDecimal totalPrice){
        try {
            ProductOrderBo productOrderBo = new ProductOrderBo(innId,contacts,phone,remark,num,price,proName,totalPrice,productId);
            ProductOrderVo order = productService.order(productOrderBo);
            return new200(order);
        } catch (Exception e) {
            LOGGER.error("下单失败",e);
            return new500(e.getMessage());
        }
    }
    @ApiOperation(value = "支付回调", notes = "支付回调", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "payResultJson", value = "接收参数实体payResultJson", required = true, dataType = "payResultJson")
    @RequestMapping(value = "/orderCallbackUrl",method = RequestMethod.POST)
    public Map<String,Object> orderCallbackUrl(@RequestBody String payResultJson){
        try{
            productService.orderCallback(payResultJson);
            return new200();
        }catch (Exception e){
            return new500(e);
        }
    }

    /*@RequestMapping(value = "/one",method = RequestMethod.GET)
    public Map<String, Object> getOne() {
        return new200(xzBaseinfoRepo.findById(3611l));
    }*/

}
