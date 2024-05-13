package radius.inmarsat.model;

import java.io.Serializable;

import radius.comm.BaseVo;

public class InmarsatVo extends BaseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381750293745240695L;

	private String id;
	private String groupName;
	private String attribute;
	private String op;
	private String value;
	
	private String addGroupName;
	private String addAttribute;
	private String addOp;
	private String addValue;
	
	private String updGroupName;
	private String updAttribute;
	private String updOp;
	private String updValue;
	
	
	
	public String getUpdGroupName() {
		return updGroupName;
	}
	public void setUpdGroupName(String updGroupName) {
		this.updGroupName = updGroupName;
	}
	public String getUpdAttribute() {
		return updAttribute;
	}
	public void setUpdAttribute(String updAttribute) {
		this.updAttribute = updAttribute;
	}
	public String getUpdOp() {
		return updOp;
	}
	public void setUpdOp(String updOp) {
		this.updOp = updOp;
	}
	public String getUpdValue() {
		return updValue;
	}
	public void setUpdValue(String updValue) {
		this.updValue = updValue;
	}
	public String getAddGroupName() {
		return addGroupName;
	}
	public void setAddGroupName(String addGroupName) {
		this.addGroupName = addGroupName;
	}
	public String getAddOp() {
		return addOp;
	}
	public void setAddOp(String addOp) {
		this.addOp = addOp;
	}
	public String getAddAttribute() {
		return addAttribute;
	}
	public void setAddAttribute(String addAttribute) {
		this.addAttribute = addAttribute;
	}
	public String getAddValue() {
		return addValue;
	}
	public void setAddValue(String addValue) {
		this.addValue = addValue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
