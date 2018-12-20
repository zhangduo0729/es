package com.hivescm.estools.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <b>Description:</b><br>
 * 发布产品详细信息 <br><br>
 * <p>
 * <b>Note</b><br>
 * <b>ProjectName:</b> b2b
 * <br><b>PackageName:</b> com.hivescm.b2b.vo
 * <br><b>Date:</b> 2017/10/30 09:31
 *
 * @author DongChunfu
 * @version 1.0
 * @since JDK 1.8
 */
public class ReleaseGoodsDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 站点ID(1 对 1)
	 */
	private Long stationId;
	/**
	 * 代理经销商
	 */
	private Long agentDealerId;
	/**
	 * 经销商ID
	 */
	private Long dealerId;
	/**
	 * orgId
	 */
	private Long dealerOrgId;
	/**
	 * 经销商名字
	 */
	private String dealerName;
	/**
	 * 商品ID
	 */
	private Long goodsId;
	/**
	 * 商品名字
	 */
	private String goodsName;
	/**
	 * 产品ID
	 */
	private Long productId;
	/**
	 * 产品名字
	 */
	private String productName;
	/**
	 * 品牌ID
	 */
	private Long brandId;
	/**
	 * 品牌名字
	 */
	private String brandName;
	/**
	 * 销售类目
	 */
	private List<SaletypeDetail> saletypes;
	/**
	 * 商品类目ID
	 */
	private Long goodsCategoryId;
	/**
	 * 商品类目名称
	 */
	private String goodsCategoryName;
	/**
	 * 企业类目ID
	 */
	private Long enterpriseCategoryId;
	/**
	 * 企业类目名称
	 */
	private String enterpriseCategoryIdName;
	/**
	 * 商品参数集合(空格隔开)
	 * <p>
	 * 产地_北京 规格_XXL
	 */
	private String goodsParams;
	/**
	 * 商品图片
	 */
	private String picsUrl;
	/**
	 * 预售价
	 */
	private Double suggestPrice;
	/**
	 * 是否新品（1是，0否，2全部）
	 */
	private Integer tagNew;
	/**
	 * 是否推荐（1是，0否，2全部）
	 */
	private Integer tagRecommend;
	/**
	 * 是否热卖（1是，0否，2全部）
	 */
	private Integer tagHot;
	/**
	 * 是否清仓（1是，0否，2全部）
	 */
	private Integer tagClear;
	/**
	 * 是否促销
	 * （是，0否）
	 */
	private Integer promotion;
	/**
	 * 销量
	 */
	private Integer salesVolume;
	/**
	 * 销售评分（）
	 */
	private Integer salesScore;
	/**
	 * 上架时间（当前时间）
	 */
	private Long shelvesTime;
	/**
	 * 商品状态
	 * 1 上架
	 * 0 下架
	 */
	private Integer status;

	/**
	 * 平台订货价
	 */
	private Double orderPrice;

	/**
	 * 起订量
	 **/
	private Integer minOrderQuantity;

	/**
	 * 起订单位
	 */
	private String quantityUnit;

	public ReleaseGoodsDetail() {
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPicsUrl() {
		return picsUrl;
	}

	public void setPicsUrl(String picsUrl) {
		this.picsUrl = picsUrl;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Long getDealerOrgId() {
		return dealerOrgId;
	}

	public void setDealerOrgId(Long dealerOrgId) {
		this.dealerOrgId = dealerOrgId;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}

	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}

	public String getGoodsCategoryName() {
		return goodsCategoryName;
	}

	public void setGoodsCategoryName(String goodsCategoryName) {
		this.goodsCategoryName = goodsCategoryName;
	}

	public Long getEnterpriseCategoryId() {
		return enterpriseCategoryId;
	}

	public void setEnterpriseCategoryId(Long enterpriseCategoryId) {
		this.enterpriseCategoryId = enterpriseCategoryId;
	}

	public String getEnterpriseCategoryIdName() {
		return enterpriseCategoryIdName;
	}

	public void setEnterpriseCategoryIdName(String enterpriseCategoryIdName) {
		this.enterpriseCategoryIdName = enterpriseCategoryIdName;
	}

	public String getGoodsParams() {
		return goodsParams;
	}

	public void setGoodsParams(String goodsParams) {
		this.goodsParams = goodsParams;
	}

	public Double getSuggestPrice() {
		return suggestPrice;
	}

	public void setSuggestPrice(Double suggestPrice) {
		this.suggestPrice = suggestPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}

	public Integer getSalesScore() {
		return salesScore;
	}

	public void setSalesScore(Integer salesScore) {
		this.salesScore = salesScore;
	}

	public Long getShelvesTime() {
		return shelvesTime;
	}

	public void setShelvesTime(Long shelvesTime) {
		this.shelvesTime = shelvesTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<SaletypeDetail> getSaletypes() {
		return saletypes;
	}

	public void setSaletypes(List<SaletypeDetail> saletypes) {
		this.saletypes = saletypes;
	}

	public Integer getTagNew() {
		return tagNew;
	}

	public void setTagNew(Integer tagNew) {
		this.tagNew = tagNew;
	}

	public Integer getTagRecommend() {
		return tagRecommend;
	}

	public void setTagRecommend(Integer tagRecommend) {
		this.tagRecommend = tagRecommend;
	}

	public Integer getTagHot() {
		return tagHot;
	}

	public void setTagHot(Integer tagHot) {
		this.tagHot = tagHot;
	}

	public Integer getTagClear() {
		return tagClear;
	}

	public void setTagClear(Integer tagClear) {
		this.tagClear = tagClear;
	}

	public Integer getPromotion() {
		return promotion;
	}

	public void setPromotion(Integer promotion) {
		this.promotion = promotion;
	}

	public Integer getMinOrderQuantity() {
		return minOrderQuantity;
	}

	public void setMinOrderQuantity(Integer minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}

	public String getQuantityUnit() {
		return quantityUnit;
	}

	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public Long getAgentDealerId() {
		return agentDealerId;
	}

	public void setAgentDealerId(Long agentDealerId) {
		this.agentDealerId = agentDealerId;
	}

	@Override
	public String toString() {
		return "ReleaseGoodsDetail{" +
				"stationId=" + stationId +
				", agentDealerId=" + agentDealerId +
				", dealerId=" + dealerId +
				", dealerOrgId=" + dealerOrgId +
				", dealerName='" + dealerName + '\'' +
				", goodsId=" + goodsId +
				", goodsName='" + goodsName + '\'' +
				", productId=" + productId +
				", productName='" + productName + '\'' +
				", brandId=" + brandId +
				", brandName='" + brandName + '\'' +
				", saletypes=" + saletypes +
				", goodsCategoryId=" + goodsCategoryId +
				", goodsCategoryName='" + goodsCategoryName + '\'' +
				", enterpriseCategoryId=" + enterpriseCategoryId +
				", enterpriseCategoryIdName='" + enterpriseCategoryIdName + '\'' +
				", goodsParams='" + goodsParams + '\'' +
				", picsUrl='" + picsUrl + '\'' +
				", suggestPrice=" + suggestPrice +
				", tagNew=" + tagNew +
				", tagRecommend=" + tagRecommend +
				", tagHot=" + tagHot +
				", tagClear=" + tagClear +
				", promotion=" + promotion +
				", salesVolume=" + salesVolume +
				", salesScore=" + salesScore +
				", shelvesTime=" + shelvesTime +
				", status=" + status +
				", orderPrice=" + orderPrice +
				", minOrderQuantity=" + minOrderQuantity +
				", quantityUnit='" + quantityUnit + '\'' +
				'}';
	}
}
