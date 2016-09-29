package com.ucap.workflow;

import java.util.List;

import com.ucap.workflow.dao.DataSource;
import com.ucap.workflow.entity.BizApp;
import com.ucap.workflow.entity.Step;
import com.ucap.workflow.entity.TplCtrl;
import com.ucap.workflow.vo.AppInfo;

public class WF {

    /*
     * 管理接口
     */
    public static void addBiz(String code, String name) {
        DataSource.addBiz( code, name );
    }

    public static void addStep(Step step) {
        DataSource.addStep( step );
    }

    public static void addTpl(String name) {
        DataSource.addTpl( name );
    }

    public static void addOrUpdateTplCtrl(int tpl_id, List<TplCtrl> ctrls) {
        DataSource.addOrUpdateTplCtrl( tpl_id, ctrls );
    }

    public static void addBizApp(BizApp app) {
        DataSource.addBizApp( app );
    }

    /*
     * 服务接口
     */
    public static String queryStatusByAppCode(String appCode) {
        return DataSource.queryStatusByAppCode( appCode );
    }

    public static AppInfo queryAppInfo(String appCode) {
        return DataSource.queryAppInfo( appCode );
    }

}
