package com.test.inventorysystem.activities;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.test.inventorysystem.R;
import com.test.inventorysystem.adapters.OfflineInvListAdapter;
import com.test.inventorysystem.db.DBHelper;
import com.test.inventorysystem.db.DBManager;
import com.test.inventorysystem.models.AssetModel;
import com.test.inventorysystem.models.OrganModel;
import com.test.inventorysystem.utils.AppContext;
import com.test.inventorysystem.utils.AssetInfoDialogUtil;
import com.test.inventorysystem.utils.ExtDate;
import com.test.inventorysystem.utils.InvAssetInfoDialogUtil;
import com.test.inventorysystem.utils.OfflineInvExistedAssetDialogUtil;
import com.test.inventorysystem.utils.Sysconfig;

import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfflineAssetInventory extends OrmLiteBaseActivity<DBHelper> implements InvAssetInfoDialogUtil.NoticeDialogListener, OfflineInvExistedAssetDialogUtil.NoticeDialogListener {

    private final static int REQUEST_CODE = 1;
    private Button scanBtn = null;
    private Button manualBtn = null;
    private Button endBtn = null;
    private Spinner inventoryOrganSpinner = null;
    private ArrayAdapter<String> organSpinnerArrayAdapter;
    private ArrayList<OrganModel> organs = new ArrayList<OrganModel>();
    private ListView listView;
    private OfflineInvListAdapter assetListAdapter = null;
    private LinearLayout mProgressBar = null;
    private DBManager dbManager = new DBManager();

    private String currAssetCode;
    private String response;
    private AssetModel currAssetModel;
    private Boolean firstTimeOpen = true;
    private Boolean manualInventory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_asset_inventory);
        initialization();
    }

    private void initialization() {
        scanBtn = (Button) findViewById(R.id.button_offline_inventory_scan);
        manualBtn = (Button) findViewById(R.id.button_offline_inventory_manual);
        endBtn = (Button) findViewById(R.id.button_offline_inventory_end);
        mProgressBar = (LinearLayout) findViewById(R.id.linearLayout_offline_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        inventoryOrganSpinner = (Spinner) findViewById(R.id.offline_spinner_inventory_organ);
        organSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        organSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inventoryOrganSpinner.setAdapter(organSpinnerArrayAdapter);
        inventoryOrganSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!firstTimeOpen) {
                    inventoryOrganSpinner.setEnabled(false);
                }
                firstTimeOpen = false;
                assetListAdapter.clear();
                try {
                    List<AssetModel> list = dbManager.findOfflineInvAssetsByOrgan(getHelper().getAssetDao(), organs.get(inventoryOrganSpinner.getSelectedItemPosition()).getOrganCode(), true);
                    assetListAdapter.addAll(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView = (ListView) findViewById(R.id.listView_offline_asset_inventory);
        assetListAdapter = new OfflineInvListAdapter(this, new ArrayList<AssetModel>());
        listView.setAdapter(assetListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AssetModel assetModel = (AssetModel) listView.getItemAtPosition(i);
                if (AppContext.offlineLogin && AppContext.hasOfflineData) {
                    DialogFragment dialogFragment = AssetInfoDialogUtil.newInstance(assetModel);
                    dialogFragment.show(getFragmentManager(), "inv_asset_update_info");
                } else {
                    DialogFragment dialogFragment = AssetInfoDialogUtil.newInstace(assetModel, "offline");
                    dialogFragment.show(getFragmentManager(), "inv_asset_update_info");
                }
            }
        });

        try {
            List<OrganModel> organList = dbManager.findOrgans(this.getHelper().getOrganDao(), AppContext.currUser.getAccounts(), null);
            if (organList.isEmpty()) {

            } else {
                for (int i = 0; i < organList.size(); i++) {
                    organs.add(organList.get(i));
                    System.out.println(organList.get(i).getOrganIDParent());
                    organSpinnerArrayAdapter.add(organList.get(i).getOrganName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCodeScanner();
            }
        });

        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assetMaunal();
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventoryOrganSpinner.setEnabled(true);
            }
        });
    }

    private void startCodeScanner() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void assetMaunal() {
        Intent intent = new Intent(this, AssetManual.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProgressBar.setVisibility(View.VISIBLE);
        if (resultCode == CaptureActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            String barcode = bundle.getString("barcode");
            if (!"".equals(barcode)) {
                String[] codes = Sysconfig.getCodes(barcode);
                String assetCode = codes[0];
                currAssetModel = new AssetModel();
                try {
                    Boolean isExisted = dbManager.findExistedOfflineInvAsset(getHelper().getAssetDao(), codes[0]);
                    String msg = "该资产已经离线盘点";
                    System.out.println(isExisted);
                    if (isExisted) {
                        DialogFragment dialogFragment = new OfflineInvExistedAssetDialogUtil().newInstance(msg);
                        dialogFragment.show(getFragmentManager(), "inv_offline_existed_asset");
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        if (AppContext.offlineLogin && AppContext.hasOfflineData) {
                            currAssetModel = dbManager.findOfflineAsset(getHelper().getAssetDao(), assetCode);
                            mProgressBar.setVisibility(View.GONE);
                            ExtDate nowdate = new ExtDate();
                            currAssetModel.setPdate(nowdate.format("yyyy-MM-dd HH:mm:ss SSS"));
                            if (StringUtils.isBlank(currAssetModel.getPdfs())) {
                                currAssetModel.setPdfs("1");
                            }
                            currAssetModel.setMgrOrganCode(AppContext.currOrgan.getOrganCode());
                            currAssetModel.setSimId(AppContext.simId);
                            DialogFragment dialogFragment = new InvAssetInfoDialogUtil().newInstance(currAssetModel);
                            dialogFragment.show(getFragmentManager(), "inv_offline_asset_info");
                            mProgressBar.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < codes.length; i++) {
                                if (i == 0) {
                                    currAssetModel.setAssetCode(codes[0]);
                                    System.out.println("test");
                                }
                                if (i == 1) {
                                    currAssetModel.setAssetName(codes[1]);
                                    System.out.println("test");
                                }
                                if (i == 2) {
                                    currAssetModel.setOperator(codes[2]);
                                    System.out.println("test");
                                }
                                if (i == 3) {
                                    currAssetModel.setOrganName(codes[3]);
                                    System.out.println("test");
                                }
                            }
                            currAssetModel.setMgrOrganCode(AppContext.currOrgan.getOrganCode());
                            currAssetModel.setOrganCode(organs.get(inventoryOrganSpinner.getSelectedItemPosition()).getOrganCode());
                            currAssetModel.setUserId(AppContext.currUser.getAccounts());
                            currAssetModel.setAddr(AppContext.address);
//                            currAssetModel.setSimId(AppContext.simId);
                            ExtDate nowdate = new ExtDate();
                            currAssetModel.setPdate(nowdate.format("yyyy-MM-dd HH:mm:ss SSS"));
                            if (StringUtils.isBlank(currAssetModel.getPdfs())) {
                                currAssetModel.setPdfs("1");
                            }
                            mProgressBar.setVisibility(View.GONE);
                            DialogFragment dialogFragment = new InvAssetInfoDialogUtil().newInstance(currAssetModel, "offline");
                            dialogFragment.show(getFragmentManager(), "inv_offline_asset_info");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == InventoryUpdate.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            AssetModel asset = (AssetModel) bundle.get("asset");
            if (null != asset) {
                doInventory(asset, asset.getOrganCode());
            }
            mProgressBar.setVisibility(View.GONE);
        } else if (resultCode == AssetManual.RESULT_CODE) {
            manualInventory = true;
            Bundle bundle = data.getExtras();
            String code = bundle.getString("code");
            currAssetModel = new AssetModel();
//            String name = bundle.getString("name");
            if (StringUtils.isNotBlank(code)) {
//                selectAssetInfo(code, "2");
                try {
                    Boolean isExisted = dbManager.findExistedOfflineInvAsset(getHelper().getAssetDao(), code);
                    if (!isExisted) {
                        currAssetModel = dbManager.findOfflineAsset(getHelper().getAssetDao(), code);
                        if (currAssetModel != null) {
                            currAssetModel.setAddr(AppContext.address);
                            currAssetModel.setSimId(AppContext.simId);
                            currAssetModel.setUserId(AppContext.currUser.getAccounts());
//                            currAssetModel.setAssetCode(code);
//                        assetModel.setAssetName(name);
                            currAssetModel.setMgrOrganCode(AppContext.currOrgan.getOrganCode());
                            currAssetModel.setOrganName(organs.get(inventoryOrganSpinner.getSelectedItemPosition()).getOrganName());
                            currAssetModel.setOfflineInv(true);
                            ExtDate nowdate = new ExtDate();
                            currAssetModel.setPdate(nowdate.format("yyyy-MM-dd HH:mm:ss SSS"));
                            currAssetModel.setPdfs("2");
                            DialogFragment dialogFragment = new InvAssetInfoDialogUtil().newInstance(currAssetModel);
                            dialogFragment.show(getFragmentManager(), "manual_offline_asset_info");
//                            doInventory(assetModel, organs.get(inventoryOrganSpinner.getSelectedItemPosition()).getOrganCode());
                            mProgressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(OfflineAssetInventory.this, "请仔细核对资产编码或者财务编码是否录入准确", Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        String msg = "该资产已经离线盘点";
                        DialogFragment dialogFragment = new OfflineInvExistedAssetDialogUtil().newInstance(msg);
                        dialogFragment.show(getFragmentManager(), "inv_offline_existed_asset");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //对应不同的dialog去判断他们的tag,以正确触发当前dialog的相应事件
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        System.out.println(dialog.getTag());
        switch (dialog.getTag()) {
            case "inv_offline_asset_info":
                doInventory(currAssetModel, currAssetModel.getOrganCode());
                break;
            case "inv_offline_existed_asset":
                startCodeScanner();
                break;
            case "manual_offline_asset_info":
                doInventory(currAssetModel, currAssetModel.getOrganCode());
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        switch (dialog.getTag()) {
            case "inv_offline_asset_info":
                updateInventory(currAssetModel);
                break;
            case "inv_offline_existed_asset":
                replaceDuplicate(currAssetModel, currAssetModel.getOrganCode());
                break;
            case "manual_offline_asset_info":
                updateInventory(currAssetModel);
        }

    }

    private void doInventory(AssetModel assetModel, String organCode) {
        try {
            dbManager.saveOfflineInvAssets(getHelper().getAssetDao(), assetModel, organCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assetListAdapter.add(assetModel);
        if (manualInventory) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OfflineAssetInventory.this);
            builder.setMessage("是否继续进行手动盘点?");

            builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    manualInventory = false;
                    Intent intent = new Intent(OfflineAssetInventory.this, AssetManual.class);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    private void replaceDuplicate(AssetModel assetModel, String organCode) {
        try {
            dbManager.saveOfflineInvAssets(getHelper().getAssetDao(), assetModel, organCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    private void updateInventory(AssetModel assetModel) {
        Intent intent = new Intent(this, InventoryUpdate.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("asset", assetModel);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

}
