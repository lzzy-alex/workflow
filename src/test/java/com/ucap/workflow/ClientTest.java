package com.ucap.workflow;

import static com.ucap.toolkit.type.IntUtil.toInt;
import static com.ucap.toolkit.type.StringUtil.str;
import static com.ucap.workflow.util.Toolkit.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ucap.workflow.entity.BizApp;
import com.ucap.workflow.entity.CtrlType;
import com.ucap.workflow.entity.Step;
import com.ucap.workflow.entity.TplCtrl;

public class ClientTest {

    private static void init() {

        /** TODO */
        WF.addTpl( "初审模板" );
        TplCtrl c1 = new TplCtrl( "意见", CtrlType.textarea.val(), 1 );
        TplCtrl c2 = new TplCtrl( "同意", CtrlType.btn_forward.val(), 2 );
        TplCtrl c3 = new TplCtrl( "退回", CtrlType.btn_stop.val(), 3 );
        WF.addOrUpdateTplCtrl( 1, Arrays.asList( c1, c2, c3 ) );

        WF.addTpl( "复审模板" );
        TplCtrl c4 = new TplCtrl( "附件", CtrlType.file.val(), 1 );
        TplCtrl c5 = new TplCtrl( "同意", CtrlType.btn_forward.val(), 2 );
        TplCtrl c6 = new TplCtrl( "退回", CtrlType.btn_stop.val(), 3 );
        WF.addOrUpdateTplCtrl( 2, Arrays.asList( c4, c5, c6 ) );

        List arg = new ArrayList( 0 );

        // initial biz
        String sql = "select proceeding_code, proceeding_name from jet_proceeding";
        for ( Object [] row : jdbc().list( sql, arg, 2 ) ) {
            WF.addBiz( str( row[0] ), str( row[1] ) );
        }

        // step
        sql = "select proceeding_code, step_id, step_name, step_index from jet_censor_step where proceeding_code in(select proceeding_code from jet_proceeding) order by proceeding_code asc, step_index asc";
        int cx = 0;
        for ( Object [] row : jdbc().list( sql, arg, 4 ) ) {
            int tpl_id = ( ( cx++ ) % 2 == 0 ) ? 1 : 2;
            WF.addStep( new Step( str( row[0] ), tpl_id, str( row[1] ), str( row[2] ), toInt( row[3] ) ) );
        }

        // biz app
        BizApp ba = new BizApp( "123456789", "08jscxgkb", "1527", BizApp.Status.pending.val() );
        WF.addBizApp( ba );

    }

    public static void main(String [] args) {
        String app_code = "123456789";
        String status = WF.queryStatusByAppCode( app_code );
        System.out.println( status );
    }

}
