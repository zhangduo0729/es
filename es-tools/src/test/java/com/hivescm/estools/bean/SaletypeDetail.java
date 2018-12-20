package com.hivescm.estools.bean;

import java.io.Serializable;

/**
 * <b>Description:</b><br>
 * 销售类目明细 <br><br>
 * <p>
 * <b>Note</b><br>
 * <b>ProjectName:</b> b2b
 * <br><b>PackageName:</b> com.hivescm.b2b.dto
 * <br><b>Date:</b> 2017/11/1 14:54
 *
 * @author DongChunfu
 * @version 1.0
 * @since JDK 1.8
 */
public class SaletypeDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 末级销售类目id
	 */
	private Long Id;

	/**
	 * 一级销售类目Id
	 */
	private Long firstSaleTypeId;
	/**
	 * 一级销售类目名称
	 */
	private String firstSaletypeName;
	/**
	 * 二级销售类目名称
	 */
	private String secondSaletypeName;
	/**
	 * 三级销售类目名称
	 */
	private String thirdSaletypeName;

	public SaletypeDetail() {
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getFirstSaletypeName() {
		return firstSaletypeName;
	}

	public void setFirstSaletypeName(String firstSaletypeName) {
		this.firstSaletypeName = firstSaletypeName;
	}

	public String getSecondSaletypeName() {
		return secondSaletypeName;
	}

	public void setSecondSaletypeName(String secondSaletypeName) {
		this.secondSaletypeName = secondSaletypeName;
	}

	public String getThirdSaletypeName() {
		return thirdSaletypeName;
	}

	public void setThirdSaletypeName(String thirdSaletypeName) {
		this.thirdSaletypeName = thirdSaletypeName;
	}

	public Long getFirstSaleTypeId() {
		return firstSaleTypeId;
	}

	public void setFirstSaleTypeId(Long firstSaleTypeId) {
		this.firstSaleTypeId = firstSaleTypeId;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SaletypeDetail{");
		sb.append("Id=").append(Id);
		sb.append(", firstSaleTypeId='").append(firstSaleTypeId).append('\'');
		sb.append(", firstSaletypeName='").append(firstSaletypeName).append('\'');
		sb.append(", secondSaletypeName='").append(secondSaletypeName).append('\'');
		sb.append(", thirdSaletypeName='").append(thirdSaletypeName).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
