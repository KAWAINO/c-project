package radius.radacctInfo.model;

import java.io.Serializable;
import java.math.BigInteger;

import radius.comm.BaseVo;

public class RadacctInfoVo extends BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4501965946659435271L;
	
	private String searchUserName;
	private String searchImsi;
	private String searchMsisdn;
	private String startDate;
	private String endDate;
	private String searchStartMin;
	private String searchEndMin;
	private String searchStartHour;
	private String searchEndHour;
	private String searchAcctStatus;
	private String searchChecked;
	private Boolean chkedAcctStatusList;
	
	private String acctStart;
	private String acctInterimUpdate;
	private String acctStop;
	
	private String acctSessionId;
	private String userName;
	private String gppImsi;
	private String msisdn;
	private String groupName;
	private String realm;
	private String nasIpAddress;
	private String nasPortId;
	private String nasPortType;
	private String acctUpdateTime;
	private String acctInterval;
	private String acctSessionTime;
	private String acctAuthentic;
	private String connectInfoStart;
	private String connectInfoStop;
	private String acctInputOctets;
	private String acctOutputOctets;
	private BigInteger acctInputPackets;
	private BigInteger acctOutputPackets;
	private String calledStationId;
	private String callingStationId;
	private String acctStatus;
	private String acctTerminateCause;
	private String serviceType;
	private String framedProtocol;
	private String framedIpAddress;
	private String gppSgsnAddress;
	private String gppGgsnAddress;
	private String gppChargingId;
	
	
	public String getSearchChecked() {
		return searchChecked;
	}
	public void setSearchChecked(String searchChecked) {
		this.searchChecked = searchChecked;
	}
	public Boolean getChkedAcctStatusList() {
		return chkedAcctStatusList;
	}
	public void setChkedAcctStatusList(Boolean chkedAcctStatusList) {
		this.chkedAcctStatusList = chkedAcctStatusList;
	}
	public String getSearchUserName() {
		return searchUserName;
	}
	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}
	public String getSearchImsi() {
		return searchImsi;
	}
	public void setSearchImsi(String searchImsi) {
		this.searchImsi = searchImsi;
	}
	public String getSearchMsisdn() {
		return searchMsisdn;
	}
	public void setSearchMsisdn(String searchMsisdn) {
		this.searchMsisdn = searchMsisdn;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSearchStartMin() {
		return searchStartMin;
	}
	public void setSearchStartMin(String searchStartMin) {
		this.searchStartMin = searchStartMin;
	}
	public String getSearchEndMin() {
		return searchEndMin;
	}
	public void setSearchEndMin(String searchEndMin) {
		this.searchEndMin = searchEndMin;
	}
	public String getSearchStartHour() {
		return searchStartHour;
	}
	public void setSearchStartHour(String searchStartHour) {
		this.searchStartHour = searchStartHour;
	}
	public String getSearchEndHour() {
		return searchEndHour;
	}
	public void setSearchEndHour(String searchEndHour) {
		this.searchEndHour = searchEndHour;
	}
	public String getSearchAcctStatus() {
		return searchAcctStatus;
	}
	public void setSearchAcctStatus(String searchAcctStatus) {
		this.searchAcctStatus = searchAcctStatus;
	}
	public String getAcctStart() {
		return acctStart;
	}
	public void setAcctStart(String acctStart) {
		this.acctStart = acctStart;
	}
	public String getAcctInterimUpdate() {
		return acctInterimUpdate;
	}
	public void setAcctInterimUpdate(String acctInterimUpdate) {
		this.acctInterimUpdate = acctInterimUpdate;
	}
	public String getAcctStop() {
		return acctStop;
	}
	public void setAcctStop(String acctStop) {
		this.acctStop = acctStop;
	}
	public String getAcctSessionId() {
		return acctSessionId;
	}
	public void setAcctSessionId(String acctSessionId) {
		this.acctSessionId = acctSessionId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGppImsi() {
		return gppImsi;
	}
	public void setGppImsi(String gppImsi) {
		this.gppImsi = gppImsi;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getNasIpAddress() {
		return nasIpAddress;
	}
	public void setNasIpAddress(String nasIpAddress) {
		this.nasIpAddress = nasIpAddress;
	}
	public String getNasPortId() {
		return nasPortId;
	}
	public void setNasPortId(String nasPortId) {
		this.nasPortId = nasPortId;
	}
	public String getNasPortType() {
		return nasPortType;
	}
	public void setNasPortType(String nasPortType) {
		this.nasPortType = nasPortType;
	}
	public String getAcctUpdateTime() {
		return acctUpdateTime;
	}
	public void setAcctUpdateTime(String acctUpdateTime) {
		this.acctUpdateTime = acctUpdateTime;
	}
	public String getAcctInterval() {
		return acctInterval;
	}
	public void setAcctInterval(String acctInterval) {
		this.acctInterval = acctInterval;
	}
	public String getAcctSessionTime() {
		return acctSessionTime;
	}
	public void setAcctSessionTime(String acctSessionTime) {
		this.acctSessionTime = acctSessionTime;
	}
	public String getAcctAuthentic() {
		return acctAuthentic;
	}
	public void setAcctAuthentic(String acctAuthentic) {
		this.acctAuthentic = acctAuthentic;
	}
	public String getConnectInfoStart() {
		return connectInfoStart;
	}
	public void setConnectInfoStart(String connectInfoStart) {
		this.connectInfoStart = connectInfoStart;
	}
	public String getConnectInfoStop() {
		return connectInfoStop;
	}
	public void setConnectInfoStop(String connectInfoStop) {
		this.connectInfoStop = connectInfoStop;
	}
	public String getAcctInputOctets() {
		return acctInputOctets;
	}
	public void setAcctInputOctets(String acctInputOctets) {
		this.acctInputOctets = acctInputOctets;
	}
	public String getAcctOutputOctets() {
		return acctOutputOctets;
	}
	public void setAcctOutputOctets(String acctOutputOctets) {
		this.acctOutputOctets = acctOutputOctets;
	}
	public BigInteger getAcctInputPackets() {
		return acctInputPackets;
	}
	public void setAcctInputPackets(BigInteger acctInputPackets) {
		this.acctInputPackets = acctInputPackets;
	}
	public BigInteger getAcctOutputPackets() {
		return acctOutputPackets;
	}
	public void setAcctOutputPackets(BigInteger acctOutputPackets) {
		this.acctOutputPackets = acctOutputPackets;
	}
	public String getCalledStationId() {
		return calledStationId;
	}
	public void setCalledStationId(String calledStationId) {
		this.calledStationId = calledStationId;
	}
	public String getCallingStationId() {
		return callingStationId;
	}
	public void setCallingStationId(String callingStationId) {
		this.callingStationId = callingStationId;
	}
	public String getAcctStatus() {
		return acctStatus;
	}
	public void setAcctStatus(String acctStatus) {
		this.acctStatus = acctStatus;
	}
	public String getAcctTerminateCause() {
		return acctTerminateCause;
	}
	public void setAcctTerminateCause(String acctTerminateCause) {
		this.acctTerminateCause = acctTerminateCause;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getFramedProtocol() {
		return framedProtocol;
	}
	public void setFramedProtocol(String framedProtocol) {
		this.framedProtocol = framedProtocol;
	}
	public String getFramedIpAddress() {
		return framedIpAddress;
	}
	public void setFramedIpAddress(String framedIpAddress) {
		this.framedIpAddress = framedIpAddress;
	}
	public String getGppSgsnAddress() {
		return gppSgsnAddress;
	}
	public void setGppSgsnAddress(String gppSgsnAddress) {
		this.gppSgsnAddress = gppSgsnAddress;
	}
	public String getGppGgsnAdress() {
		return gppGgsnAddress;
	}
	public void setGppGgsnAdress(String gppGgsnAdress) {
		this.gppGgsnAddress = gppGgsnAdress;
	}
	public String getGppChargingId() {
		return gppChargingId;
	}
	public void setGppChargingId(String gppChargingId) {
		this.gppChargingId = gppChargingId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
