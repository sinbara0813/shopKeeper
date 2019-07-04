package com.d2cmall.shopkeeper.api;

import android.util.ArrayMap;

import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.common.OrderRequest;
import com.d2cmall.shopkeeper.model.AllotDetialBean;
import com.d2cmall.shopkeeper.model.AllotItemDetialBean;
import com.d2cmall.shopkeeper.model.AllotItemListBean;
import com.d2cmall.shopkeeper.model.AllotListBean;
import com.d2cmall.shopkeeper.model.AllotSettleBean;
import com.d2cmall.shopkeeper.model.AllotSkuBean;
import com.d2cmall.shopkeeper.model.BankInfoBean;
import com.d2cmall.shopkeeper.model.BankListBean;
import com.d2cmall.shopkeeper.model.BannerBean;
import com.d2cmall.shopkeeper.model.BrowseListBean;
import com.d2cmall.shopkeeper.model.CartBean;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.model.CouponBean;
import com.d2cmall.shopkeeper.model.CouponInsertProductBean;
import com.d2cmall.shopkeeper.model.CouponListBean;
import com.d2cmall.shopkeeper.model.CustomerListBean;
import com.d2cmall.shopkeeper.model.DraweeBean;
import com.d2cmall.shopkeeper.model.EmptyBean;
import com.d2cmall.shopkeeper.model.FeedBackBean;
import com.d2cmall.shopkeeper.model.HomeBean;
import com.d2cmall.shopkeeper.model.MarginBean;
import com.d2cmall.shopkeeper.model.MarketProductListBean;
import com.d2cmall.shopkeeper.model.OrderDetialBean;
import com.d2cmall.shopkeeper.model.OrderListBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ProductListBean;
import com.d2cmall.shopkeeper.model.PurchaseDetailBean;
import com.d2cmall.shopkeeper.model.PurchaseItemDetialBean;
import com.d2cmall.shopkeeper.model.PurchaseItemListBean;
import com.d2cmall.shopkeeper.model.PurchaseListBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.StaffListBean;
import com.d2cmall.shopkeeper.model.StatBean;
import com.d2cmall.shopkeeper.model.StatListBean;
import com.d2cmall.shopkeeper.model.StockDataBean;
import com.d2cmall.shopkeeper.model.TransactionRecordListBean;
import com.d2cmall.shopkeeper.model.UndemolitionListBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.model.WithdrawBean;
import com.d2cmall.shopkeeper.model.WithdrawRecordListBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 作者:Created by sinbara on 2019/2/13.
 * 邮箱:hrb940258169@163.com
 */

public interface ApiServer {

    //获取banner
    @POST("/b_api/module/demo")
    Observable<BaseModel<BannerBean>> getBanner();

    //商品品类业务
    @POST("b_api/product_category/delete/{id}")
    Observable<BaseModel<EmptyBean>> categoryDelete(@Path("id") long id);

    @POST("b_api/product_category/insert")
    Observable<BaseModel<CategoryBean>> categoryInsert(@Body CategoryBean categoryBean);

    @GET("b_api/product_category/list")
    Observable<BaseModel<List<CategoryBean>>> getCategoryList(@QueryMap ArrayMap<String, String> map);

    @POST("b_api/product_category/update")
    Observable<BaseModel<CategoryBean>> categoryUpdate(@Body CategoryBean categoryBean);

    @GET("b_api/product_category/{id}")
    Observable<BaseModel<CategoryBean>> getCategory(@Path("id") int id);

    //商品分类
    @GET("b_api/product_classify/list")
    Observable<BaseModel<List<CategoryBean>>> getClassifyList(@QueryMap ArrayMap<String, String> map);

    //商品业务
    @POST("b_api/product/insert")
    Observable<BaseModel<ProductBean>> productInsert(@Body ProductBean productBean);

    @GET("b_api/product/list")
    Observable<BaseModel<ProductListBean>> getProductList(@QueryMap ArrayMap<String,String> map);

    @POST("b_api/product/update")
    Observable<BaseModel<ProductBean>> productUpdate(@Body ProductBean productBean);

    @GET("b_api/product/{id}")
    Observable<BaseModel<ProductBean>> getProduct(@Path("id") long id);

    @POST("b_api/product/status")
    @FormUrlEncoded
    Observable<BaseModel<EmptyBean>> productStatus(@Field("id")long id,@Field("status") int status);

    @POST("b_api/product/delete/{id}")
    Observable<BaseModel<EmptyBean>> productDelete(@Path("id") long id);

    @GET("/b_api/product/page") //选货市场商品详情
    Observable<BaseModel<MarketProductListBean>> getMarketProductList(@QueryMap ArrayMap<String,String> map);

    //购物车
    @POST("/b_api/package/delete")
    @FormUrlEncoded
    Observable<BaseModel<EmptyBean>> cartDelete(@Field("ids") String ids);

    @POST("/b_api/package/insert")
    @FormUrlEncoded
    Observable<BaseModel<EmptyBean>> cartInsert(@Field("skuId") long skuId,@Field("quantity") int num);

    @POST("/b_api/package/update")
    @FormUrlEncoded
    Observable<BaseModel<EmptyBean>> cartUpdate(@Field("id") long id,@Field("quantity") int num);

    @GET("/b_api/package/list")
    Observable<BaseModel<List<CartBean>>> getCartList(@QueryMap ArrayMap<String,String> map);

    @GET("/b_api/package/count")
    Observable<BaseModel<String>> getCartCount();

    //线下收银
    @POST("/b_api/order/settle")
    Observable<BaseModel<OrderDetialBean>> offlineOrderSettle(@Body OrderRequest orderRequest);

    @POST("/b_api/order/create")
    Observable<BaseModel<OrderDetialBean>> offlineOrderCreate(@Body OrderRequest orderRequest);

    @POST("/b_api/order/pay")
    @FormUrlEncoded
    Observable<BaseModel<EmptyBean>> orderPay(@FieldMap ArrayMap<String,String> map);

    @POST("/b_api/drawee/insert") //添加付款人
    Observable<BaseModel<DraweeBean>> draweeInsert(@Body DraweeBean bean);

    @POST("/b_api/drawee/update") //更新付款人
    Observable<BaseModel<DraweeBean>> draweeUpdate(@Body DraweeBean bean);

    //免费拿货
    @POST("/b_api/allot/settle")
    Observable<BaseModel<AllotListBean.RecordsBean>> allotOrderSettle(@Body OrderRequest orderRequest);

    @POST("/b_api/allot/create")
    Observable<BaseModel<AllotListBean.RecordsBean>> allotOrderCreate(@Body OrderRequest orderRequest);

    @POST("/b_api/allot/statement") //货品结算
    Observable<BaseModel<List<AllotSkuBean>>> allotStatement();

    @POST("/b_api/allot/statement/create")
    @FormUrlEncoded
    Observable<BaseModel<AllotSettleBean>> allotStatementCreate(@Field("shopId") long shopId);

    @POST("/b_api/allot/statement/pay")
    @FormUrlEncoded
    Observable<BaseModel<EmptyBean>> allotStatementPay(@FieldMap ArrayMap<String,String> map);

    //采购
    @POST("/b_api/purch/settle")
    Observable<BaseModel<PurchaseListBean.RecordsBean>> purchaseOrderSettle(@Body OrderRequest orderRequest);

    @POST("/b_api/purch/create")
    Observable<BaseModel<PurchaseListBean.RecordsBean>> purchaseOrderCreate(@Body OrderRequest orderRequest);

    @POST("/b_api/purch/pay")
    @FormUrlEncoded
    Observable<BaseModel<EmptyBean>> purchasePay(@FieldMap ArrayMap<String,String> map);

    //店铺业务
    @POST("b_api/shop/insert")
    Observable<BaseModel<ShopBean>> createShop(@Body ShopBean shopBean);

    //注册
    @POST("b_api/shopkeeper/register")
    @FormUrlEncoded
    Observable<BaseModel<UserBean>> register(@FieldMap ArrayMap<String,String> map);

    //登录
    @POST("b_api/shopkeeper/login")
    @FormUrlEncoded
    Observable<BaseModel<UserBean>> login(@FieldMap ArrayMap<String,String> map);

    //重置密码
    @POST("b_api/shopkeeper/password")
    @FormUrlEncoded
    Observable<BaseModel<UserBean>> resetPassword(@FieldMap ArrayMap<String,String> map);

    //退出登录
    @POST("/b_api/shopkeeper/logout")
    Observable<BaseModel<EmptyBean>> loginout();

    //短信业务
    @POST("b_api/message/sms")
    @FormUrlEncoded
    Observable<BaseModel<EmptyBean>> getSecurityCode(@FieldMap ArrayMap<String,String> map);

    //客户
    @GET("b_api/member/list")
    Observable<BaseModel<CustomerListBean>> getCustomerList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/member/focus/list")
    Observable<BaseModel<CustomerListBean>> getCustomerFocusList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/member/browse/list")
    Observable<BaseModel<BrowseListBean>> getBrowseList(@QueryMap ArrayMap<String,String> map);

    //编辑用户信息
    @POST("b_api/shopkeeper/update")
    @FormUrlEncoded
    Observable<BaseModel<UserBean>> updatePersonInfo(@FieldMap ArrayMap<String,String> map);

    @GET("b_api/shopkeeper/{id}")
    Observable<BaseModel<StaffListBean.RecordsBean>> getStaffInfo(@Path("id") long id);

    //订单业务
    @GET("b_api/order/list")//订单列表未拆单
    Observable<BaseModel<UndemolitionListBean>> getUndemolitionOrderList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/order_item/list")//订单列表已拆单
    Observable<BaseModel<OrderListBean>> getOrderList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/order_item/{id}")//订单详情
    Observable<BaseModel<OrderDetialBean>> getOrderDetial(@Path("id") long id);


    @POST("b_api/order_item/update/amount")//修改订单金额
    @FormUrlEncoded
    Observable<BaseModel> updateOrderAmount(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/order/update/address")//修改收货地址
    @FormUrlEncoded
    Observable<BaseModel> updateOrderAddress(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/order_item/deliver")
    @FormUrlEncoded
    Observable<BaseModel> deliverGood(@FieldMap ArrayMap<String,String> map);

    //店铺
    @POST("b_api/shop/update")
    Observable<BaseModel<ShopBean>> updateShop(@Body ShopBean shopBean);

    @GET("b_api/shop/{id}")
    Observable<BaseModel<ShopBean>> getBrandInfo(@Path("id") long id);

    //店员业务
    @GET("b_api/shopkeeper/list")
    Observable<BaseModel<StaffListBean>> getStaffList(@QueryMap ArrayMap<String, String> map);

    @POST("b_api/shopkeeper/insert")
    Observable<BaseModel<StaffListBean.RecordsBean>> insertStaff(@Body StaffListBean.RecordsBean staffBean);

    //提现业务GET
    @GET("b_api/shop_withdraw/list")
    Observable<BaseModel<WithdrawRecordListBean>> getWithdrawRecordList(@QueryMap ArrayMap<String,String> map);

    //交易记录
    @GET("b_api/shop_flow/list")
    Observable<BaseModel<TransactionRecordListBean>> getTransactionRecordList(@QueryMap ArrayMap<String,String> map);

    //保证金记录
    @GET("/b_api/shop_deposit/list")
    Observable<BaseModel<MarginBean>> getMarginList(@QueryMap ArrayMap<String,String> map);

    //优惠券
    @POST("b_api/coupon/insert")//创建优惠券
    Observable<BaseModel<CouponBean>> insertCoupon(@Body CouponBean couponBean);

    @POST("b_api/coupon/product/insert")//优惠券绑定商品
    Observable<BaseModel<ArrayList<CouponInsertProductBean>>> couponBindProduct(@Body ArrayList<CouponInsertProductBean> couponBeans);

    @GET("b_api/coupon/list") //优惠券列表
    Observable<BaseModel<CouponListBean>> getCouponList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/coupon/{id}") //优惠券详情
    Observable<BaseModel<CouponBean>> getCouponDetial(@Path("id") Long id);

    @POST("b_api/coupon/product/delete")//解绑优惠券
    @FormUrlEncoded
    Observable<BaseModel> unbindProduct(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/coupon/product/relation")//查询优惠券已绑定商品
    @FormUrlEncoded
    Observable<BaseModel<ArrayList<Long>>> couponRelationProduct(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/coupon/update")//更新优惠券
    Observable<BaseModel<CouponBean>> upDateCoupon(@Body CouponBean couponBean);

    @GET("b_api/coupon/cancel/{id}")//核销优惠券
    Observable<BaseModel> cancleCoupon(@Path("id")  Long id);

    @POST("b_api/shop_withdraw/insert")//更新优惠券
    Observable<BaseModel<WithdrawBean>> applyWithdraw(@Body WithdrawBean withdrawBean);

    @GET("b_api/home/info")//首页数据
    Observable<BaseModel<HomeBean>> getHomeInfo();

    @POST("b_api/product/crowd")//改变拼团状态
    @FormUrlEncoded
    Observable<BaseModel> changeCrowdStatus(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/feedback/insert")//意见反馈
    Observable<BaseModel> insertFeedBack(@Body FeedBackBean feedBackBean);

    @POST("b_api/bank_card/insert")//新增银行卡
    Observable<BaseModel<BankInfoBean>> insertBankCard(@Body BankInfoBean bankInfoBean);

    @POST("b_api/bank_card/update")//更新银行卡
    Observable<BaseModel<BankInfoBean>> updateBankCard(@Body BankInfoBean bankInfoBean);

    @GET("b_api/bank_card/list")//订单列表已拆单
    Observable<BaseModel<BankListBean>> getBankList(@QueryMap ArrayMap<String,String> map);

    @POST("b_api/order_item/agree/refund")//同意退款
    @FormUrlEncoded
    Observable<BaseModel> agreeRefund(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/order_item/success/refund")//同意退款
    @FormUrlEncoded
    Observable<BaseModel> refundSuccess(@FieldMap ArrayMap<String,String> map);


    @POST("b_api/order_item/agree/reship")//同意退货退款退款
    @FormUrlEncoded
    Observable<BaseModel> agreeReship(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/order_item/refuse/reship")//拒绝退货退款退款
    @FormUrlEncoded
    Observable<BaseModel> refuseReship(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/order_item/refuse/refund")//拒绝退款
    @FormUrlEncoded
    Observable<BaseModel> refuseRefund(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/order_item/receive/reship")//退货退款确认收货
    @FormUrlEncoded
    Observable<BaseModel> receiveSure(@FieldMap ArrayMap<String,String> map);

    @GET("b_api/allot/list")//调拨单未拆单列表
    Observable<BaseModel<AllotListBean>> getAllotList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/allot_item/list")//调拨单已拆单列表
    Observable<BaseModel<AllotItemListBean>> getAllotItemList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/allot/{id}")//调拨单未拆单详情
    Observable<BaseModel<AllotDetialBean>> getAllotDetial(@Path("id") long id);

    @GET("b_api/allot_item/{id}")//调拨单已拆单详情
    Observable<BaseModel<AllotItemDetialBean>> getAllotItemDetial(@Path("id") long id);

    @POST("b_api/allot/cancel")//调拨单取消
    @FormUrlEncoded
    Observable<BaseModel<AllotListBean.RecordsBean>> allotCancel(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/allot_item/receive")//调拨单一件入库
    @FormUrlEncoded
    Observable<BaseModel<String>> allotReceive(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/allot_item/cancel/difference")//调拨单取消异常
    @FormUrlEncoded
    Observable<BaseModel> allotCancelDiff(@FieldMap ArrayMap<String,String> map);


    @GET("b_api/purch/list")//采购单列表
    Observable<BaseModel<PurchaseListBean>> getPurchList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/purch_item/list")//采购单列表
    Observable<BaseModel<PurchaseItemListBean>> getPurchItemList(@QueryMap ArrayMap<String,String> map);

    @GET("b_api/purch/{id}")//采购单未拆单详情
    Observable<BaseModel<PurchaseDetailBean>> getPurchDetial(@Path("id") long id);

    @GET("b_api/purch_item/{id}")//采购单已拆单详情
    Observable<BaseModel<PurchaseItemDetialBean>> getPurchItemDetial(@Path("id") long id);

    @POST("b_api/purch/cancel")//采购单取消
    @FormUrlEncoded
    Observable<BaseModel<AllotListBean.RecordsBean>> purchaseCancel(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/purch_item/receive")//采购单一件入库
    @FormUrlEncoded
    Observable<BaseModel<String>> purchReceive(@FieldMap ArrayMap<String,String> map);

    @POST("b_api/purch_item/cancel/difference")//采购单取消异常
    @FormUrlEncoded
    Observable<BaseModel> purchCancelDiff(@FieldMap ArrayMap<String,String> map);


    @GET("b_api/home/count")//进货管理数据
    Observable<BaseModel<StockDataBean>> getStockData();

    @GET("b_api/statistics/getStatisticsAllEnergizeShopInfo") //普通用户统计信息
    Observable<BaseModel<StatBean>> getStatFormTime(@QueryMap ArrayMap<String,String> map);

    @GET("/b_api/statistics/getStatisticsAllShopInfo") //管理员统计信息
    Observable<BaseModel<StatBean>> getStatAll(@QueryMap ArrayMap<String,String> map);

    @GET("/b_api/statistics/getStatisticsPageInfo") //所有门店统计信息
    Observable<BaseModel<StatListBean>> getStatAllShop(@QueryMap ArrayMap<String,String> map);

    @GET("/b_api/statistics/getStatisticsInfo") //某个门店统计信息
    Observable<BaseModel<StatBean>> getStatShop(@QueryMap ArrayMap<String,String> map);
}
