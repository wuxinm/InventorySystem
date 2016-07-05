package com.test.inventorysystem.models;

import com.google.gson.JsonObject;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by youmengli on 6/12/16.
 */

@DatabaseTable(tableName = "asset")
public class AssetModel implements Serializable {

    @DatabaseField(id = true)
    private String pid = "";
    @DatabaseField
    private String dt = "";
    @DatabaseField
    private String inventoryInfo = "";
    @DatabaseField
    private String pdate = "";
    @DatabaseField
    private String userId = "";
    @DatabaseField
    private String assetParentID = "";
    @DatabaseField
    private String assetType = "";
    @DatabaseField
    private String cateID = "";
    @DatabaseField
    private String createdBy = "";
    @DatabaseField
    private String deprType = "";
    @DatabaseField
    private String lastUpdateBy = "";
    @DatabaseField
    private String liablePerson = "";
    @DatabaseField
    private String mgrOrganID = "";
    @DatabaseField
    private String organID = "";
    @DatabaseField
    private String spec = "";
    @DatabaseField
    private String unit = "";
    @DatabaseField
    private String assetName = "";
    @DatabaseField
    private String enableDateString = "";
    @DatabaseField
    private String status = "";
    @DatabaseField
    private String useAge;
    @DatabaseField
    private String mgrOrganName = "";
    @DatabaseField
    private String mgrOrganCode = "";
    @DatabaseField
    private String addr = "";
    @DatabaseField
    private String simId = "";
    @DatabaseField
    private Double originalValue;
    @DatabaseField
    private String assetCode = "";
    @DatabaseField
    private String storage = "";
    @DatabaseField
    private String storageDescr = "";
    @DatabaseField
    private String operator = "";
    @DatabaseField
    private String findCode;
    @DatabaseField
    private String finCode;
    @DatabaseField
    private String assetTypeName = "";
    @DatabaseField
    private String organName = "";
    @DatabaseField
    private String organCode = "";
    @DatabaseField
    private String cateName = "";
    @DatabaseField
    private String invMsg;
    @DatabaseField
    private String pdfs = "";
    @DatabaseField
    private String disCodes = "";
    @DatabaseField
    private Double starNum;
    @DatabaseField
    private String invNote;
    @DatabaseField
    private Boolean offlineInv;

    public AssetModel () { super(); }

    public AssetModel (JsonObject jsonObject) {
        super();
        setAssetName(jsonObject.get("assetName").getAsString());
        setEnableDateString(jsonObject.get("enableDateString").getAsString());
        setStatus(jsonObject.get("status").getAsString());
        setUseAge(jsonObject.get("useAge").getAsString());
        setMgrOrganName(jsonObject.get("mgrOrganName").getAsString());
        setOriginalValue(jsonObject.get("originalValue").getAsDouble());
        setAssetCode(jsonObject.get("assetCode").getAsString());
        setStorageDescr(jsonObject.get("storageDescr").getAsString());
        setOperator(jsonObject.get("operator").getAsString());
        setFindCode(jsonObject.get("findCode").getAsString());
        setAssetTypeName(jsonObject.get("assetTypeName").getAsString());
//        setAssetId(jsonObject.get("assetID").getAsString());
        setCateID(jsonObject.get("cateId").getAsString());
        setOrganName(jsonObject.get("organName").getAsString());
        setCateName(jsonObject.get("cateName").getAsString());
    }

    // if current object is inventory result
    public AssetModel (JsonObject jsonObject, String string) {
        super();
        setAssetName(jsonObject.get("assetName").getAsString());
        setEnableDateString(jsonObject.get("enableDateString").getAsString());
        setStatus(jsonObject.get("status").getAsString());
        setUseAge(jsonObject.get("useAge").getAsString());
        setMgrOrganName(jsonObject.get("mgrOrganName").getAsString());
        setOriginalValue(jsonObject.get("originalValue").getAsDouble());
        setAssetCode(jsonObject.get("assetCode").getAsString());
        setStorage(jsonObject.get("storage").getAsString());
        setStorageDescr(jsonObject.get("storageDescr").getAsString());
        setOperator(jsonObject.get("operator").getAsString());
        setFinCode(jsonObject.get("finCode").getAsString());
        setAssetTypeName(jsonObject.get("assetTypeName").getAsString());
        setOrganName(jsonObject.get("organName").getAsString());
        setCateName(jsonObject.get("cateName").getAsString());
        setSpec(jsonObject.get("spec").getAsString());
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setEnableDateString(String enableDateString) {
        this.enableDateString = enableDateString;
    }

    public String getEnableDateString() {
        return enableDateString;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setUseAge(String useAge) {
        this.useAge = useAge;
    }

    public String getUseAge() {
        return useAge;
    }

    public void setMgrOrganName(String mgrOrganName) {
        this.mgrOrganName = mgrOrganName;
    }

    public String getMgrOrganName() {
        return mgrOrganName;
    }

    public void setOriginalValue(Double originalValue) {
        this.originalValue = originalValue;
    }

    public Double getOriginalValue() {
        return originalValue;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorageDescr(String storageDescr) {
        this.storageDescr = storageDescr;
    }

    public String getStorageDescr() {
        return storageDescr;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setFindCode(String findCode) {
        this.findCode = findCode;
    }

    public String getFindCode() {
        return findCode;
    }

    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }

    public String getAssetTypeName() {
        return assetTypeName;
    }

//    public void setAssetId(String assetId) {
//        this.assetId = assetId;
//    }
//
//    public String getAssetId() {
//        return assetId;
//    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganName() {
        return organName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateName() {
        return cateName;
    }

    public void setInvMsg(String invMsg) {
        this.invMsg = invMsg;
    }

    public String getInvMsg() {
        return invMsg;
    }

    public void setPdfs(String pdfs) {
        this.pdfs = pdfs;
    }

    public String getPdfs() {
        return pdfs;
    }

    public void setDisCode(String disCodes) {
        this.disCodes = disCodes;
    }

    public String getDisCode() {
        return disCodes;
    }

    public void setStarNum(Double starNum) {
        this.starNum = starNum;
    }

    public Double getStarNum() {
        return starNum;
    }

    public void setInvNote(String invNote) {
        this.invNote = invNote;
    }

    public String getInvNote() {
        return invNote;
    }

    public void setOfflineInv(Boolean offlineInv) {
        this.offlineInv = offlineInv;
    }

    public Boolean getOfflineInv() {
        return offlineInv;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAddr() {
        return addr;
    }

    public void setMgrOrganCode(String mgrOrganCode) {
        this.mgrOrganCode = mgrOrganCode;
    }

    public String getMgrOrganCode() {
        return mgrOrganCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getSimId() {
        return simId;
    }

    public void setAssetParentID(String assetParentID) {
        this.assetParentID = assetParentID;
    }

    public String getAssetParentID() {
        return assetParentID;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setCateID(String cateID) {
        this.cateID = cateID;
    }

    public String getCateID() {
        return cateID;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setDeprType(String deprType) {
        this.deprType = deprType;
    }

    public String getDeprType() {
        return deprType;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDt() {
        return dt;
    }

    public void setInventoryInfo(String inventoryInfo) {
        this.inventoryInfo = inventoryInfo;
    }

    public String getInventoryInfo() {
        return inventoryInfo;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLiablePerson(String liablePerson) {
        this.liablePerson = liablePerson;
    }

    public String getLiablePerson() {
        return liablePerson;
    }

    public void setMgrOrganID(String mgrOrganID) {
        this.mgrOrganID = mgrOrganID;
    }

    public String getMgrOrganID() {
        return mgrOrganID;
    }

    public void setOrganID(String organID) {
        this.organID = organID;
    }

    public String getOrganID() {
        return organID;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getPdate() {
        return pdate;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSpec() {
        return spec;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setFinCode(String finCode) {
        this.finCode = finCode;
    }

    public String getFinCode() {
        return finCode;
    }
}
